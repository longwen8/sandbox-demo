package com.longwen.server.app.core.util.matcer;

import com.longwen.server.app.api.filter.ExtFilter;
import com.longwen.server.app.api.filter.Filter;

/**
 * Created by huangxinping on 19/11/18.
 */
public class ExtFilterMatcher implements Matcher {
    private final ExtFilter extFilter;

    public ExtFilterMatcher(final ExtFilter extFilter){
        this.extFilter = extFilter;
    }

    public static Matcher toOrGroupMatcher(final Filter[] filterArray) {
        return null;
    }
}
