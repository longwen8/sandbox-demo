package com.longwen.server.app.api.resource;

import com.longwen.server.app.api.event.Event;
import com.longwen.server.app.api.event.EventListener;
import com.longwen.server.app.api.filter.Filter;
import com.longwen.server.app.api.listener.ext.EventWatchBuilder;
import com.longwen.server.app.api.listener.ext.EventWatchCondition;

/**
 * 事件观察者
 */
public interface ModuleEventWatcher {

    int watch(Filter filter, EventListener listener, Progress progress ,Event.Type ... eventType);

    int watch(Filter filter, EventListener listener, Event.Type ... eventType);

    int watch(EventWatchCondition eventWatchCondition, EventListener eventListener, Progress progress, Event.Type ... eventType);




















    interface Progress {

        /**
         * 进度开始
         * @param total
         */
        void begin(int total);

        void progressOnSuccess(Class clazz, int index);

        void progressOnFailed(Class clazz, int index, Throwable cause);

        void finish(int cCnt, int mCnt);

    }
}
