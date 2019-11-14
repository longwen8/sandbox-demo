package com.longwen.server.app.core.manager.impl;


import com.longwen.server.app.api.Module;
import com.longwen.server.app.api.ModuleException;
import com.longwen.server.app.core.CoreModule;
import com.longwen.server.app.core.classloader.ModuleJarClassLoader;
import com.longwen.server.app.core.manager.CoreLoadedClassDataSource;
import com.longwen.server.app.core.manager.CoreModuleManager;
import com.longwen.server.app.core.manager.ProviderManager;
import com.longwen.server.jetty.CoreConfigure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultCoreModuleManager implements CoreModuleManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final CoreConfigure cfg;
    private final CoreLoadedClassDataSource classDataSource;
    private final ProviderManager providerManager;
    //模块目录
    private final File[] moduleLibDirArray;

    private final Map<String,CoreModule> loadedModuleBOMap = new ConcurrentHashMap<String,CoreModule>();


    public DefaultCoreModuleManager(final CoreConfigure cfg,
                                    final CoreLoadedClassDataSource classDataSource,
                                    final ProviderManager providerManager){
        this.cfg = cfg;
        this.classDataSource = classDataSource;
        this.providerManager = providerManager;
        this.moduleLibDirArray = mergeFileArray(
                new File[]{new File(cfg.getSystemModuleLibPath())},
                cfg.getUserModuleLibFilesWithCache()
        );
    }
    private File[] mergeFileArray(File[] aFileArray, File[] bFileArray) {
        final List<File> _r = new ArrayList<File>();
        _r.addAll(Arrays.asList(aFileArray));
        _r.addAll(Arrays.asList(bFileArray));
        return _r.toArray(new File[]{});
    }


    @Override
    public synchronized CoreModuleManager reset() throws ModuleException {

        logger.info("resetting all loaded modules:{}", loadedModuleBOMap.keySet());

        // 1. 强制卸载所有模块
       // unloadAll();

        // 2. 加载所有模块
        for (final File moduleLibDir : moduleLibDirArray) {
            // 用户模块加载目录，加载用户模块目录下的所有模块
            // 对模块访问权限进行校验
            if (moduleLibDir.exists() && moduleLibDir.canRead()) {
                new ModuleLibLoader(moduleLibDir, cfg.getLaunchMode())
                        .load(
                                new InnerModuleJarLoadCallback(),
                                new InnerModuleLoadCallback()
                        );
            } else {
                logger.warn("module-lib not access, ignore flush load this lib. path={}", moduleLibDir);
            }
        }

        return this;
    }

    /**
     * 加载并注册模块
     * <p>1. 如果模块已经存在则返回已经加载过的模块</p>
     * <p>2. 如果模块不存在，则进行常规加载</p>
     * <p>3. 如果模块初始化失败，则抛出异常</p>
     *
     * @param uniqueId          模块ID
     * @param module            模块对象
     * @param moduleJarFile     模块所在JAR文件
     * @param moduleClassLoader 负责加载模块的ClassLoader
     * @throws ModuleException 加载模块失败
     */
    private synchronized void load(final String uniqueId,
                                   final Module module,
                                   final File moduleJarFile,
                                   final ModuleJarClassLoader moduleClassLoader) throws ModuleException {

        if (loadedModuleBOMap.containsKey(uniqueId)) {
            logger.debug("module already loaded. module={};", uniqueId);
            return;
        }

        logger.info("loading module, module={};class={};module-jar={};",
                uniqueId,
                module.getClass().getName(),
                moduleJarFile
        );

        // 初始化模块信息
        final CoreModule coreModule = new CoreModule(uniqueId, moduleJarFile, moduleClassLoader, module);

        // 注入@Resource资源
       // injectResourceOnLoadIfNecessary(coreModule);

       // callAndFireModuleLifeCycle(coreModule, MODULE_LOAD);

        // 设置为已经加载
        coreModule.markLoaded(true);

        // 如果模块标记了加载时自动激活，则需要在加载完成之后激活模块
       // markActiveOnLoadIfNecessary(coreModule);

        // 注册到模块列表中
        loadedModuleBOMap.put(uniqueId, coreModule);

        // 通知生命周期，模块加载完成
        //callAndFireModuleLifeCycle(coreModule, MODULE_LOAD_COMPLETED);

    }

    @Override
    public CoreModule get(String uniqueId) {
        return loadedModuleBOMap.get(uniqueId);
    }




    /**
     * 用户模块文件加载回调
     */
    final private class InnerModuleJarLoadCallback implements ModuleLibLoader.ModuleJarLoadCallback {

        @Override
        public void onLoad(File moduleJarFile) throws Throwable {
            System.out.println("用户文件加载回调");
            providerManager.loading(moduleJarFile);

        }
    }



    /**
     * 用户模块加载回调
     */
    final private class InnerModuleLoadCallback implements ModuleJarLoader.ModuleLoadCallback {

        @Override
        public void onLoad(String uniqueId, Class moduleClass, Module module, File moduleJarFile, ModuleJarClassLoader moduleClassLoader) throws Throwable {
            System.out.println("用户模块加载回调");

            // 如果之前已经加载过了相同ID的模块，则放弃当前模块的加载
            if (loadedModuleBOMap.containsKey(uniqueId)) {
                final CoreModule existedCoreModule = get(uniqueId);
                logger.info("IMLCB: module already loaded, ignore load this module. expected:module={};class={};loader={}|existed:class={};loader={};",
                        uniqueId,
                        moduleClass, moduleClassLoader,
                        existedCoreModule.getModule().getClass().getName(),
                        existedCoreModule.getLoader()
                );
                return;
            }

            // 需要经过ModuleLoadingChain的过滤
            providerManager.loading(
                    uniqueId,
                    moduleClass,
                    module,
                    moduleJarFile,
                    moduleClassLoader
            );

            // 之前没有加载过，这里进行加载
            logger.info("IMLCB: found new module, prepare to load. module={};class={};loader={};",
                    uniqueId,
                    moduleClass,
                    moduleClassLoader
            );

            // 这里进行真正的模块加载
            load(uniqueId, module, moduleJarFile, moduleClassLoader);
        }
    }

}
