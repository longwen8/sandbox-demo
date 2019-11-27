package com.longwen.server.jetty;

import com.longwen.server.CoreServer;
import com.longwen.server.app.core.JvmSandbox;
import com.longwen.server.servlet.HelloServlet;
import com.longwen.server.servlet.ModuleHttpServlet;
import com.longwen.server.util.Initializer;
import com.longwen.server.util.LogbackUtils;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;


import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.net.InetSocketAddress;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static java.lang.String.format;
import static com.longwen.server.util.NetworkUtils.isPortInUsing;
import static org.eclipse.jetty.servlet.ServletContextHandler.NO_SESSIONS;

public class JettyCoreServer implements CoreServer {

    private static volatile CoreServer coreServer;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Initializer initializer = new Initializer(true);

    private Server httpServer;
    private CoreConfigure cig;
    private JvmSandbox jvmSandbox;

    public static CoreServer getInstance(){
        if(null == coreServer){
            synchronized (CoreServer.class){
                if(null == coreServer){
                    coreServer = new JettyCoreServer();
                }
            }
        }
        return coreServer;
    }

    @Override
    public boolean isBind() {
        return initializer.isInitialized();
    }

    @Override
    public void unbind() throws IOException {
        try {

            initializer.destroyProcess(new Initializer.Processor() {
                @Override
                public void process() throws Throwable {

                    if (null != httpServer) {

                        // stop http server
                        logger.info("{} is stopping", JettyCoreServer.this);
                        httpServer.stop();

                    }

                }
            });

            // destroy http server
            logger.info("{} is destroying", this);
            while (!httpServer.isStopped());
            httpServer.destroy();

        } catch (Throwable cause) {
            logger.warn("{} unBind failed.", this, cause);
            throw new IOException("unBind failed.", cause);
        }
    }

    @Override
    public synchronized void bind(final CoreConfigure cfg, final Instrumentation inst) throws IOException {

        this.cig = cfg;
        try {
            initializer.initProcess(new Initializer.Processor() {
                @Override
                public void process() throws Throwable {

                    LogbackUtils.init(
                            cig.getNamespace(),
                            cig.getCfgLibPath() + File.separator + "sandbox-logback.xml"
                    );
                    logger.info("initializing server. ");
                    //jvmSandbox = new JvmSandbox(cfg, inst);
                    jvmSandbox = new JvmSandbox(cig,inst);
                    initHttpServer();
                    initJettyContextHandler();
                    httpServer.start();
                }
            });

            // 初始化加载所有的模块
            try {
                jvmSandbox.getCoreModuleManager().reset();
            } catch (Throwable cause) {
                logger.warn("reset occur error when initializing.", cause);
            }

            final InetSocketAddress local = getLocal();
            logger.info("initialized server. actual bind to {}:{}",
                    local.getHostName(),
                    local.getPort()
            );

        } catch (Throwable cause) {

            // 这里会抛出到目标应用层，所以在这里留下错误信息
            logger.warn("initialize server failed.", cause);

            // 对外抛出到目标应用中
            throw new IOException("server bind failed.", cause);
        }

        logger.info("{} bind success.", this);

    }

    @Override
    public InetSocketAddress getLocal() throws IOException {
        if (!isBind()
                || null == httpServer) {
            throw new IOException("server was not bind yet.");
        }

        SelectChannelConnector scc = null;
        final Connector[] connectorArray = httpServer.getConnectors();
        if (null != connectorArray) {
            for (final Connector connector : connectorArray) {
                if (connector instanceof SelectChannelConnector) {
                    scc = (SelectChannelConnector) connector;
                    break;
                }//if
            }//for
        }//if

        if (null == scc) {
            throw new IllegalStateException("not found SelectChannelConnector");
        }

        return new InetSocketAddress(
                scc.getHost(),
                scc.getLocalPort()
        );
    }

    @Override
    public void destory() {
        // 关闭HTTP服务器
        if (isBind()) {
            try {
                unbind();
            } catch (IOException e) {
                logger.warn("{} unBind failed when destroy.", this, e);
            }
        }
    }

    //----------------------------------------- 启动 server ---------------------------------

    private void initHttpServer() {

        final String serverIp = cig.getServerIp();
        final int serverPort = cig.getServerPort();

        // 如果IP:PORT已经被占用，则无法继续被绑定
        // 这里说明下为什么要这么无聊加个这个判断，让Jetty的Server.bind()抛出异常不是更好么？
        // 比较郁闷的是，如果这个端口的绑定是"SO_REUSEADDR"端口可重用的模式，那么这个server是能正常启动，但无法正常工作的
        // 所以这里必须先主动检查一次端口占用情况，当然了，这里也会存在一定的并发问题，BUT，我认为这种概率事件我可以选择暂时忽略
        if (isPortInUsing(serverIp, serverPort)) {
            throw new IllegalStateException(format("address[%s:%s] already in using, server bind failed.",
                    serverIp,
                    serverPort
            ));
        }

        httpServer = new Server(new InetSocketAddress(serverIp, serverPort));
        QueuedThreadPool qtp = new QueuedThreadPool();
        // jetty线程设置为daemon，防止应用启动失败进程无法正常退出
       // qtp.setDaemon(true);
        qtp.setName("sandbox-jetty-qtp-" + qtp.hashCode());
        httpServer.setThreadPool(qtp);
    }


    private void initJettyContextHandler() {
        final ServletContextHandler context = new ServletContextHandler(NO_SESSIONS);
        final String contextPath = "/sandbox";
        context.setContextPath(contextPath);
        context.setClassLoader(getClass().getClassLoader());


        context.addServlet(new ServletHolder(new HelloServlet()), "/hello");

//        // web-socket-servlet
//        final String wsPathSpec = "/module/websocket/*";
//        logger.info("initializing ws-http-handler. path={}", contextPath + wsPathSpec);
//        //noinspection deprecation
//        context.addServlet(
//                new ServletHolder(new WebSocketAcceptorServlet(jvmSandbox.getCoreModuleManager())),
//                wsPathSpec
//        );



        // moule-http-servlet
        final String pathSpec = "/module/http/*";
        context.addServlet(
                new ServletHolder(new ModuleHttpServlet(cig,jvmSandbox.getCoreModuleManager())),
                pathSpec
        );

        httpServer.setHandler(context);
    }

}
