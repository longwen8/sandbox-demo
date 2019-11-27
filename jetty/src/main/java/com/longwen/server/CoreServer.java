package com.longwen.server;

import com.longwen.server.jetty.CoreConfigure;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.net.InetSocketAddress;

/**
 * 定义一个内核服务器
 */
public interface CoreServer {

    /**
     * 判断服务器是否已经绑定端口
     * @return
     */
    boolean isBind();

    /**
     * 服务器解除端口绑定
     * @throws IOException
     */
    void unbind() throws IOException;

    /**
     * 获取服务器绑定本地网络信息
     * @return
     * @throws IOException
     */
    InetSocketAddress getLocal() throws IOException;


    /**
     * 服务器信息绑定
     * @throws IOException
     */
    void bind(CoreConfigure cig,Instrumentation inst)throws IOException;

    /**
     * 销毁服务器
     */
    void destory();

}
