package com.longwen.server.jetty;

import com.longwen.server.app.api.Information;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Collection;
import java.util.LinkedHashSet;

/**
 *
 */
public class CoreConfigure {

    private static final String KEY_NAMESPACE = "namespace";
    private static final String DEFAULT_VAL_NAMESPACE = "default";

    private static final String KEY_SANDBOX_HOME = "sandbox_home";
    private static final String KEY_LAUNCH_MODE = "mode";
    private static final String KEY_SERVER_IP = "server.ip";
    private static final String KEY_SERVER_PORT = "server.port";
    private static final String KEY_SERVER_CHARSET = "server.charset";

    private static final String KEY_SYSTEM_MODULE_LIB_PATH = "system_module";
    private static final String KEY_USER_MODULE_LIB_PATH = "user_module";
    private static final String KEY_PROVIDER_LIB_PATH = "provider";
    private static final String KEY_CFG_LIB_PATH = "cfg";
    private static final String VAL_LAUNCH_MODE_AGENT = "agent";
    private static final String VAL_LAUNCH_MODE_ATTACH = "attach";

    private static final String KEY_UNSAFE_ENABLE = "unsafe.enable";

    private static volatile CoreConfigure instance;

    public static CoreConfigure toConfigure(final String featureString, final String propertiesFilePath) {
       //return instance = mergePropertiesFile(new CoreConfigure(featureString), propertiesFilePath);
        if(null == instance){
            instance = new CoreConfigure();

        }
        return instance;
    }

    public String getServerIp() {
        return  "127.0.0.1";
    }

    public int getServerPort(){
        return 8080;
    }


    /**
     * 获取系统模块加载路径
     *
     * @return 模块加载路径
     */
    public String getSystemModuleLibPath() {
        //return featureMap.get(KEY_SYSTEM_MODULE_LIB_PATH);
        return "E:\\sandbox\\module";
    }


    /**
     * 获取用户模块加载路径
     *
     * @return 用户模块加载路径
     */
    public String getUserModuleLibPath() {
        //return featureMap.get(KEY_USER_MODULE_LIB_PATH);
        return "";
    }

    // 用户模块加载文件/目录缓存集合
    private volatile File[] GET_USER_MODULE_LIB_FILES_CACHE = null;

    /**
     * 从缓存中获取用户模块加载文件/目录
     *
     * @return 用户模块加载文件/目录
     */
    public File[] getUserModuleLibFilesWithCache() {
        if (null != GET_USER_MODULE_LIB_FILES_CACHE) {
            return GET_USER_MODULE_LIB_FILES_CACHE;
        } else {
            return getUserModuleLibFiles();
        }
    }


    /**
     * 获取用户模块加载文件/目录(集合)
     *
     * @return 用户模块加载文件/目录(集合)
     */
    public synchronized File[] getUserModuleLibFiles() {

        final Collection<File> foundModuleJarFiles = new LinkedHashSet<File>();
        for (final String path : getUserModuleLibPaths()) {
            final File fileOfPath = new File(path);
            if (fileOfPath.isDirectory()) {
                foundModuleJarFiles.addAll(FileUtils.listFiles(new File(path), new String[]{"jar"}, false));
            } else {
                if (StringUtils.endsWithIgnoreCase(fileOfPath.getPath(), ".jar")) {
                    foundModuleJarFiles.add(fileOfPath);
                }
            }
        }

        return GET_USER_MODULE_LIB_FILES_CACHE = foundModuleJarFiles.toArray(new File[]{});
    }

    /**
     * 获取用户模块加载路径(集合)
     *
     * @return 用户模块加载路径(集合)
     */
    public String[] getUserModuleLibPaths() {
        //return replaceWithSysPropUserHome(codec.toCollection(featureMap.get(KEY_USER_MODULE_LIB_PATH)).toArray(new String[]{}));
        return new String[]{"E:\\sandbox\\sandbox-module"};
    }



    /**
     * 获取沙箱内部服务提供库目录
     *
     * @return 沙箱内部服务提供库目录
     */
    public String getProviderLibPath() {
        return "E:\\sandbox\\provider";
    }


    /**
     * 获取沙箱的启动模式
     * 默认按照ATTACH模式启动
     *
     * @return 沙箱的启动模式
     */
    public Information.Mode getLaunchMode() {

        return Information.Mode.ATTACH;
    }



    public String getCfgLibPath(){
        return "E:\\sandbox\\cfg";
    }

    public String getNamespace(){
        return "default";
    }
}
