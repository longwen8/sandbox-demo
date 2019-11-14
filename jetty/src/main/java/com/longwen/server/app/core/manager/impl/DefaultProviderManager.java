package com.longwen.server.app.core.manager.impl;

import com.longwen.server.app.api.Module;
import com.longwen.server.app.core.classloader.ProviderClassLoader;
import com.longwen.server.app.core.manager.ProviderManager;
import com.longwen.server.app.core.provider.api.ModuleJarLoadingChain;
import com.longwen.server.app.core.provider.api.ModuleLoadingChain;
import com.longwen.server.jetty.CoreConfigure;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ServiceLoader;

public class DefaultProviderManager implements ProviderManager {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Collection<ModuleJarLoadingChain> moduleJarLoadingChains = new ArrayList<ModuleJarLoadingChain>();
    private final Collection<ModuleLoadingChain> moduleLoadingChains = new ArrayList<ModuleLoadingChain>();
    private final CoreConfigure cfg;


    public DefaultProviderManager(final CoreConfigure cfg) {
        this.cfg = cfg;
        try {
            init(cfg);
        } catch (Throwable cause) {
            logger.warn("loading sandbox's provider-lib[{}] failed.", cfg.getProviderLibPath(), cause);
        }
    }

    private void init(final CoreConfigure cfg) {
        final File providerLibDir = new File(cfg.getProviderLibPath());
        if (!providerLibDir.exists()
                || !providerLibDir.canRead()) {
            logger.warn("loading provider-lib[{}] was failed, doest existed or access denied.", providerLibDir);
            return;
        }

        for (final File providerJarFile : FileUtils.listFiles(providerLibDir, new String[]{"jar"}, false)) {

            try {
                final ProviderClassLoader providerClassLoader = new ProviderClassLoader(providerJarFile, getClass().getClassLoader());

                // load ModuleJarLoadingChain
                inject(moduleJarLoadingChains, ModuleJarLoadingChain.class, providerClassLoader, providerJarFile);

                // load ModuleLoadingChain
                inject(moduleLoadingChains, ModuleLoadingChain.class, providerClassLoader, providerJarFile);

                logger.info("loading provider-jar[{}] was success.", providerJarFile);
            } catch (IllegalAccessException cause) {
                logger.warn("loading provider-jar[{}] occur error, inject provider resource failed.", providerJarFile, cause);
            } catch (IOException ioe) {
                logger.warn("loading provider-jar[{}] occur error, ignore load this provider.", providerJarFile, ioe);
            }

        }
    }


    private <T> void inject(final Collection<T> collection,
                            final Class<T> clazz,
                            final ClassLoader providerClassLoader,
                            final File providerJarFile) throws IllegalAccessException {
        final ServiceLoader<T> serviceLoader = ServiceLoader.load(clazz, providerClassLoader);
        for (final T provider : serviceLoader) {
           // injectResource(provider);
            collection.add(provider);
            logger.info("loading provider[{}] was success from provider-jar[{}], impl={}",
                    clazz.getName(), providerJarFile, provider.getClass().getName());
        }
    }


    @Override
    public void loading(final File moduleJarFile) throws Throwable {
        for (final ModuleJarLoadingChain chain : moduleJarLoadingChains) {
            chain.loading(moduleJarFile);
        }
    }



    @Override
    public void loading(String uniqueId, Class moduleClass, Module module, File moduleJarFile, ClassLoader moduleClassLoader) throws Throwable {

    }
}
