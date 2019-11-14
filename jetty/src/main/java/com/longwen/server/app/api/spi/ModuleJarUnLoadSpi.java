package com.longwen.server.app.api.spi;

public interface ModuleJarUnLoadSpi {

    /**
     * 模块Jar文件卸载完所有模块后，正式卸载Jar文件之前调用！
     */
    void onJarUnLoadCompleted();
}
