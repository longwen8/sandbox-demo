package com.longwen.server.app.api.listener.ext;

import com.longwen.server.app.api.filter.Filter;

public interface EventWatchCondition {


    Filter[] getOrFilterArray();
}
