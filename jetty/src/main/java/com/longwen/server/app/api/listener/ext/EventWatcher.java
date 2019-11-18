package com.longwen.server.app.api.listener.ext;

public interface EventWatcher extends EventWatchBuilder.IBuildingForUnWatching {

    int getWatchId();
}
