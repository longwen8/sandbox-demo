package com.longwen.server.app.core.util.matcer;

import com.longwen.server.app.core.util.matcer.structure.ClassStructure;

/**
 * Created by huangxinping on 19/11/18.
 */
public interface Matcher {

    /**
     * 匹配类结构
     *
     * @param classStructure 类结构
     * @return 匹配结果
     */
    MatchingResult matching(ClassStructure classStructure);
}
