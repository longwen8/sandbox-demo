package com.longwen.server.app.core.util.matcer;

import com.longwen.server.app.core.util.matcer.structure.BehaviorStructure;

import java.util.LinkedHashSet;

/**
 * Created by huangxinping on 19/11/18.
 */
public class MatchingResult {

    private final LinkedHashSet<BehaviorStructure> behaviorStructures = new LinkedHashSet<BehaviorStructure>();

    /**
     * 是否匹配成功
     *
     * @return TRUE:匹配成功;FALSE:匹配失败;
     */
    public boolean isMatched() {
        return !behaviorStructures.isEmpty();
    }

    /**
     * 获取匹配上的行为列表
     *
     * @return 匹配上的行为列表
     */
    public LinkedHashSet<BehaviorStructure> getBehaviorStructures() {
        return behaviorStructures;
    }

    /**
     * 获取匹配上的行为签名列表
     *
     * @return 行为签名列表
     */
    public LinkedHashSet<String> getBehaviorSignCodes() {
        final LinkedHashSet<String> behaviorSignCodes = new LinkedHashSet<String>();
        for (BehaviorStructure behaviorStructure : behaviorStructures) {
            behaviorSignCodes.add(behaviorStructure.getSignCode());
        }
        return behaviorSignCodes;
    }
}
