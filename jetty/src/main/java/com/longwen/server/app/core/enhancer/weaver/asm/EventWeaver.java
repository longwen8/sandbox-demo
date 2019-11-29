package com.longwen.server.app.core.enhancer.weaver.asm;


import com.longwen.server.app.api.event.Event;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static com.longwen.server.util.SandboxStringUtils.toJavaClassName;
import static org.apache.commons.lang3.ArrayUtils.contains;

/**
 * 方法事件编织者
 * Created by luanjia@taobao.com on 16/7/16.
 */
public class EventWeaver extends ClassVisitor implements Opcodes {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final int targetClassLoaderObjectID;
    private final String namespace;
    private final int listenerId;
    private final String targetJavaClassName;
    private final Set<String> signCodes;
    private final Event.Type[] eventTypeArray;

    // 是否支持LINE_EVENT
    // LINE_EVENT需要对Class做特殊的增强，所以需要在这里做特殊的判断
    private final boolean isLineEnable;

    // 是否支持CALL_BEFORE/CALL_RETURN/CALL_THROWS事件
    // CALL系列事件需要对Class做特殊的增强，所以需要在这里做特殊的判断
    private final boolean hasCallThrows;
    private final boolean hasCallBefore;
    private final boolean hasCallReturn;
    private final boolean isCallEnable;

    public EventWeaver(final int api,
                       final ClassVisitor cv,
                       final String namespace,
                       final int listenerId,
                       final int targetClassLoaderObjectID,
                       final String targetClassInternalName,
                       final Set<String/*BehaviorStructure#getSignCode()*/> signCodes,
                       final Event.Type[] eventTypeArray) {
        super(api, cv);
        this.targetClassLoaderObjectID = targetClassLoaderObjectID;
        this.namespace = namespace;
        this.listenerId = listenerId;
        this.targetJavaClassName = toJavaClassName(targetClassInternalName);
        this.signCodes = signCodes;
        this.eventTypeArray = eventTypeArray;

        this.isLineEnable = contains(eventTypeArray, Event.Type.LINE);
        this.hasCallBefore = contains(eventTypeArray, Event.Type.CALL_BEFORE);
        this.hasCallReturn = contains(eventTypeArray, Event.Type.CALL_RETURN);
        this.hasCallThrows = contains(eventTypeArray, Event.Type.CALL_THROWS);
        this.isCallEnable = hasCallBefore || hasCallReturn || hasCallThrows;
    }

}