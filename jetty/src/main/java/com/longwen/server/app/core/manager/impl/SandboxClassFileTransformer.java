package com.longwen.server.app.core.manager.impl;

import com.longwen.server.app.api.event.Event;
import com.longwen.server.app.api.event.EventListener;
import com.longwen.server.app.core.util.ObjectIDs;
import com.longwen.server.app.core.util.matcer.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

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

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        return new byte[0];
    }
}
