package com.longwen.server.app.core.manager;


import com.longwen.server.app.api.ModuleException;
import com.longwen.server.app.core.CoreModule;

public interface CoreModuleManager {


    /**
     * 沙箱重置
     *
     * @return this
     * @throws ModuleException 沙箱重置失败
     */
    CoreModuleManager reset() throws ModuleException;

    /**
     * 获取模块
     *
     * @param uniqueId 模块ID
     * @return 模块
     */
    CoreModule get(String uniqueId);
}
