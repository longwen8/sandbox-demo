package com.longwen.app.module;

import com.longwen.server.app.api.Command;
import com.longwen.server.app.api.Information;
import com.longwen.server.app.api.Module;
import com.longwen.server.app.api.ProcessController;
import com.longwen.server.app.api.listener.ext.Advice;
import com.longwen.server.app.api.listener.ext.AdviceListener;
import com.longwen.server.app.api.listener.ext.EventWatchBuilder;
import com.longwen.server.app.api.resource.ModuleEventWatcher;
import org.kohsuke.MetaInfServices;

import javax.annotation.Resource;

/**
 * Created by huangxinping on 19/11/18.
 */
@MetaInfServices(Module.class)
@Information(id = "sandbox-test", version = "0.0.4", author = "mxphuang@gmail.com")
public class MyClockModule implements Module{

    @Resource
    private ModuleEventWatcher moduleEventWatcher;

    @Command("checkClock")
    public void repairCheckState() {

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
                        ProcessController.returnImmediately(null);
                    }
                });

    }
}
