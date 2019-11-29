package com.longwen.server.app.core;

import com.longwen.server.app.core.manager.CoreModuleManager;
import com.longwen.server.app.core.manager.impl.DefaultCoreModuleManager;
import com.longwen.server.app.core.manager.impl.DefaultLoadedClassDataSource;
import com.longwen.server.app.core.manager.impl.DefaultProviderManager;
import com.longwen.server.jetty.CoreConfigure;

import java.lang.instrument.Instrumentation;

public class JvmSandbox {


    private final CoreConfigure cfg;
    private final CoreModuleManager coreModuleManager;
    public JvmSandbox(final CoreConfigure cfg,final Instrumentation inst){
      //  EventListenerHandlers.getSingleton();
        this.cfg = cfg;
        this.coreModuleManager = new DefaultCoreModuleManager(cfg,inst,
                new DefaultLoadedClassDataSource(inst,false),
                new DefaultProviderManager(cfg));
    }

    /**
     *  获取模块管理器
     * @return
     */
    public CoreModuleManager getCoreModuleManager() {
        return coreModuleManager;
    }

}
