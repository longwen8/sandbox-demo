package com.longwen.server.app.core.manager.impl;

import com.longwen.server.app.api.event.Event;
import com.longwen.server.app.api.event.EventListener;
import com.longwen.server.app.core.classloader.ModuleJarClassLoader;
import com.longwen.server.app.core.util.ObjectIDs;
import com.longwen.server.app.core.util.matcer.Matcher;
import com.longwen.server.app.core.util.matcer.MatchingResult;
import com.longwen.server.app.core.util.matcer.UnsupportedMatcher;
import com.longwen.server.app.core.util.matcer.structure.ClassStructure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Set;

import static com.longwen.server.app.core.util.matcer.structure.ClassStructureFactory.*;

public class SandboxClassFileTransformer implements ClassFileTransformer {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final int watchId;
    private final String uniqueId;
    private final Matcher matcher;
    private final EventListener eventListener;
    private final boolean isEnableUnsafe;
    private final Event.Type[] eventTypeArray;

    private final String namespace;
    private final int listenerId;
    private final AffectStatistic affectStatistic = new AffectStatistic();

    SandboxClassFileTransformer(final int watchId,
                                final String uniqueId,
                                final Matcher matcher,
                                final EventListener eventListener,
                                final boolean isEnableUnsafe,
                                final Event.Type[] eventTypeArray,
                                final String namespace) {
        this.watchId = watchId;
        this.uniqueId = uniqueId;
        this.matcher = matcher;
        this.eventListener = eventListener;
        this.isEnableUnsafe = isEnableUnsafe;
        this.eventTypeArray = eventTypeArray;
        this.namespace = namespace;
        this.listenerId = ObjectIDs.instance.identity(eventListener);
    }

    // 获取当前类结构
    private ClassStructure getClassStructure(final ClassLoader loader,
                                             final Class<?> classBeingRedefined,
                                             final byte[] srcByteCodeArray) {
        return null == classBeingRedefined
                ? createClassStructure(srcByteCodeArray, loader)
                : createClassStructure(classBeingRedefined);
    }

    @Override
    public byte[] transform(ClassLoader loader,
                            String internalClassName,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] srcByteCodeArray) throws IllegalClassFormatException {
        try {

            // 这里过滤掉Sandbox所需要的类，防止ClassCircularityError的发生
            if (null != internalClassName
                    && internalClassName.startsWith("com/alibaba/jvm/sandbox/")) {
                return null;
            }

            // 这里过滤掉来自SandboxClassLoader的类，防止ClassCircularityError的发生
            if (loader == SandboxClassFileTransformer.class.getClassLoader()) {
                return null;
            }

            // 过滤掉来自ModuleJarClassLoader加载的类
            if (loader instanceof ModuleJarClassLoader) {
                return null;
            }

            return _transform(
                    loader,
                    internalClassName,
                    classBeingRedefined,
                    srcByteCodeArray
            );


        } catch (Throwable cause) {
            logger.warn("sandbox transform {} in loader={}; failed, module={} at watch={}, will ignore this transform.",
                    internalClassName,
                    loader,
                    uniqueId,
                    watchId,
                    cause
            );
            return null;
        }
    }

    private byte[] _transform(final ClassLoader loader,
                              final String internalClassName,
                              final Class<?> classBeingRedefined,
                              final byte[] srcByteCodeArray) {
        // 如果未开启unsafe开关，是不允许增强来自BootStrapClassLoader的类
        if (!isEnableUnsafe
                && null == loader) {
            logger.debug("transform ignore {}, class from bootstrap but unsafe.enable=false.", internalClassName);
            return null;
        }

        final ClassStructure classStructure = getClassStructure(loader, classBeingRedefined, srcByteCodeArray);
        final MatchingResult matchingResult = new UnsupportedMatcher(loader, isEnableUnsafe).and(matcher).matching(classStructure);
        final Set<String> behaviorSignCodes = matchingResult.getBehaviorSignCodes();

        // 如果一个行为都没匹配上也不用继续了
        if (!matchingResult.isMatched()) {
            logger.debug("transform ignore {}, no behaviors matched in loader={}", internalClassName, loader);
            return null;
        }

        // 开始进行类匹配
        try {
            final byte[] toByteCodeArray = new EventEnhancer().toByteCodeArray(
                    loader,
                    srcByteCodeArray,
                    behaviorSignCodes,
                    namespace,
                    listenerId,
                    eventTypeArray
            );
            if (srcByteCodeArray == toByteCodeArray) {
                logger.debug("transform ignore {}, nothing changed in loader={}", internalClassName, loader);
                return null;
            }

            // statistic affect
            affectStatistic.statisticAffect(loader, internalClassName, behaviorSignCodes);

            logger.info("transform {} finished, by module={} in loader={}", internalClassName, uniqueId, loader);
            return toByteCodeArray;
        } catch (Throwable cause) {
            logger.warn("transform {} failed, by module={} in loader={}", internalClassName, uniqueId, loader, cause);
            return null;
        }
    }
}
