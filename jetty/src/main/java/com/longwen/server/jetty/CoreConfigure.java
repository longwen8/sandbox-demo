package com.longwen.server.jetty;

import org.apache.commons.lang3.StringUtils;

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



}
