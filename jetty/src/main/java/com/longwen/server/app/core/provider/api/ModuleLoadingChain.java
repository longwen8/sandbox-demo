package com.longwen.server.app.core.provider.api;



import com.longwen.server.app.api.Module;

import java.io.File;

public interface ModuleLoadingChain {

    void loading(final String uniqueId, final Class moduleClass, final Module module, final File moduleJarFile,
                 final ClassLoader moduleClassLoader) throws Throwable;
}
