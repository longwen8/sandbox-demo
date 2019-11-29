package com.longwen.server.app.core.manager.impl;

import com.longwen.server.app.api.event.Event;
import com.longwen.server.app.api.event.EventListener;
import com.longwen.server.app.api.filter.Filter;
import com.longwen.server.app.api.listener.ext.EventWatchCondition;
import com.longwen.server.app.api.resource.ModuleEventWatcher;
import com.longwen.server.app.core.CoreModule;
import com.longwen.server.app.core.manager.CoreLoadedClassDataSource;
import com.longwen.server.app.core.util.matcer.ExtFilterMatcher;
import com.longwen.server.app.core.util.matcer.Matcher;
import com.longwen.server.util.Sequencer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;
import java.util.List;

import static com.longwen.server.app.api.filter.ExtFilter.ExtFilterFactory.make;
import static com.longwen.server.app.core.util.matcer.ExtFilterMatcher.toOrGroupMatcher;

/**
 * Created by huangxinping on 19/11/18.
 */
public class DefaultModuleEventWatcher implements ModuleEventWatcher{

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Instrumentation inst;
    private final CoreLoadedClassDataSource classDataSource;
    private final CoreModule coreModule;
    private final boolean isEnableUnsafe;
    private final String namespace;

    // 观察ID序列生成器
    private final Sequencer watchIdSequencer = new Sequencer();


    DefaultModuleEventWatcher(final Instrumentation inst,
                              final CoreLoadedClassDataSource classDataSource,
                              final CoreModule coreModule,
                              final boolean isEnableUnsafe,
                              final String namespace) {
        this.inst = inst;
        this.classDataSource = classDataSource;
        this.coreModule = coreModule;
        this.isEnableUnsafe = isEnableUnsafe;
        this.namespace = namespace;
    }

    @Override
    public int watch(Filter filter, EventListener listener, Event.Type... eventType) {
        return watch(filter,listener,null,eventType);
    }

    @Override
    public int watch(Filter filter, EventListener listener, Progress progress, Event.Type... eventType) {
        return watch(new ExtFilterMatcher(make(filter)), listener, progress, eventType);
    }

    @Override
    public int watch(EventWatchCondition eventWatchCondition, EventListener eventListener, Progress progress, Event.Type... eventType) {

        return watch(toOrGroupMatcher(eventWatchCondition.getOrFilterArray()), eventListener, progress, eventType);
    }

    private int watch(final Matcher matcher,
                      final EventListener listener,
                      final Progress progress,
                      final Event.Type... eventType){
        final int watchId = watchIdSequencer.next();
        logger.info("watchId={} 执行watch方法，module={}", watchId ,coreModule.getUniqueId());
        logger.info("EventListener={} progress={} eventtype={}", listener ,progress,eventType);
        //给对应的模块追加ClassFileTransfomer
        final SandboxClassFileTransformer sandClassFileTransformer = new SandboxClassFileTransformer(
                watchId, coreModule.getUniqueId(), matcher, listener, isEnableUnsafe, eventType, namespace);
        coreModule.getSandboxClassFileTransformers().add(sandClassFileTransformer);

        // 注册到JVM加载上ClassFileTransformer处理新增的类
        inst.addTransformer(sandClassFileTransformer, true);
        final List<Class<?>> waitingReTransformClasses = classDataSource.findForReTransform(matcher);
        logger.info("watch={} in module={} found {} classes for watch(ing).",
                watchId,
                coreModule.getUniqueId(),
                waitingReTransformClasses.size()
        );

        return watchId;

    }
}
