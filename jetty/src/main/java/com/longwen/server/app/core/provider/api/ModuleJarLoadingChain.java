package com.longwen.server.app.core.provider.api;

import java.io.File;

public interface ModuleJarLoadingChain {


    void loading(File moduleJarFile) throws Throwable;
}
