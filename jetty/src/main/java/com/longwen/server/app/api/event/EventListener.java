package com.longwen.server.app.api.event;

public interface EventListener {

    void onEvent(Event event) throws Throwable;
}
