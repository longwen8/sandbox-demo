package com.longwen.server.app.api;

import static com.longwen.server.app.api.ProcessControlException.State.*;
import static com.longwen.server.app.api.ProcessControlException.throwReturnImmediately;
import static com.longwen.server.app.api.ProcessControlException.throwThrowsImmediately;

public class ProcessController {

    /**
     * 中断当前代码处理流程,并立即返回指定对象
     *
     * @param object 返回对象
     * @throws ProcessControlException 抛出立即返回流程控制异常
     */
    public static void returnImmediately(final Object object) throws ProcessControlException {
        throwReturnImmediately(object);
    }

    /**
     * 中断当前代码处理流程,并抛出指定异常
     *
     * @param throwable 指定异常
     * @throws ProcessControlException 抛出立即抛出异常流程控制异常
     */
    public static void throwsImmediately(final Throwable throwable) throws ProcessControlException {
        throwThrowsImmediately(throwable);
    }

    /**
     * 中断当前代码处理流程,并立即返回指定对象,且忽略后续所有事件处理
     *
     * @param object 返回对象
     * @throws ProcessControlException 抛出立即返回流程控制异常
     * @since {@code sandbox-api:1.0.16}
     */
    public static void returnImmediatelyWithIgnoreProcessEvent(final Object object) throws ProcessControlException {
        throw new ProcessControlException(true, RETURN_IMMEDIATELY, object);
    }

    /**
     * 中断当前代码处理流程,并抛出指定异常,且忽略后续所有事件处理
     *
     * @param throwable 指定异常
     * @throws ProcessControlException 抛出立即抛出异常流程控制异常
     * @since {@code sandbox-api:1.0.16}
     */
    public static void throwsImmediatelyWithIgnoreProcessEvent(final Throwable throwable) throws ProcessControlException {
        throw new ProcessControlException(true, THROWS_IMMEDIATELY, throwable);
    }

    private static final ProcessControlException noneImmediatelyException
            = new ProcessControlException(NONE_IMMEDIATELY, null);

    private static final ProcessControlException noneImmediatelyWithIgnoreProcessEventException
            = new ProcessControlException(true, NONE_IMMEDIATELY, null);

    /**
     * 不干预当前处理流程
     *
     * @throws ProcessControlException 抛出不干预流程处理异常
     * @since {@code sandbox-api:1.0.16}
     */
    public static void noneImmediately() throws ProcessControlException {
        throw noneImmediatelyException;
    }

    /**
     * 不干预当前处理流程,但忽略后续所有事件处理
     *
     * @throws ProcessControlException 抛出不干预流程处理异常
     * @since {@code sandbox-api:1.0.16}
     */
    public static void noneImmediatelyWithIgnoreProcessEvent() throws ProcessControlException {
        throw noneImmediatelyWithIgnoreProcessEventException;
    }
}
