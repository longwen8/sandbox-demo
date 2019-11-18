package com.longwen.server.app.api.listener.ext;

import com.longwen.server.app.api.event.Event;
import com.longwen.server.app.api.event.EventListener;

public class AdviceAdapterListener implements EventListener {

    private final AdviceListener adviceListener;

    public AdviceAdapterListener(final AdviceListener adviceListener) {
        this.adviceListener = adviceListener;
    }

    @Override
    public void onEvent(Event event) throws Throwable {

    }
}
