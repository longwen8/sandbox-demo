package com.longwen.server.app.core;

import com.longwen.server.app.core.manager.CoreModuleManager;
import com.longwen.server.app.core.manager.impl.DefaultCoreModuleManager;
import com.longwen.server.jetty.CoreConfigure;

public class JvmSandbox {


    private final CoreConfigure cfg;
    private final CoreModuleManager coreModuleManager;
    public JvmSandbox(final CoreConfigure cfg){
        this.cfg = cfg;
        this.coreModuleManager = new DefaultCoreModuleManager();
    }

    /**
     *  获取模块管理器
     * @return
     */
    public CoreModuleManager getCoreModuleManager() {
        return coreModuleManager;
    }

}
