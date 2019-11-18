package com.longwen.server.jetty;

import com.longwen.server.app.api.event.Event;
import com.longwen.server.app.api.listener.ext.Advice;
import com.longwen.server.app.api.listener.ext.AdviceListener;
import com.longwen.server.app.api.listener.ext.EventWatchBuilder;
import com.longwen.server.app.api.resource.ModuleEventWatcher;
import com.longwen.server.app.api.test.MockForBuilderModuleEventWatcher;
import com.longwen.server.util.ApiQaArrayUtils;

public class EventStart {

    public static void main(String[] args) {

         new EventStart().test();

    }

    private void test(){
        final MockForBuilderModuleEventWatcher mockForBuilderModuleEventWatcher
                = new MockForBuilderModuleEventWatcher();
        new EventWatchBuilder(mockForBuilderModuleEventWatcher)
                .onClass("java.lang.String")
                .onBehavior("toString")
                .onWatch(new AdviceListener());
        System.out.println(mockForBuilderModuleEventWatcher.getEventTypeArray().length);
        System.out.println(ApiQaArrayUtils.has(Event.Type.RETURN, mockForBuilderModuleEventWatcher.getEventTypeArray()));
        System.out.println(mockForBuilderModuleEventWatcher.getEventWatchCondition().getOrFilterArray().length);

    }

    private void test2(){
        ModuleEventWatcher moduleEventWatcher = null;

        new EventWatchBuilder(moduleEventWatcher)
                .onClass("com.longwen.clock.Clock")
                .onBehavior("checkState")
                .onWatch(new AdviceListener() {

                    /**
                     * 拦截{@code com.longwen.clock.Clock#checkState()}方法，当这个方法抛出异常时将会被
                     * AdviceListener#afterThrowing()所拦截
                     */
                    @Override
                    protected void afterThrowing(Advice advice) throws Throwable {

                        // 在此，你可以通过ProcessController来改变原有方法的执行流程
                        // 这里的代码意义是：改变原方法抛出异常的行为，变更为立即返回；void返回值用null表示
                        //ProcessController.returnImmediately(null);
                    }
                });
    }
}
