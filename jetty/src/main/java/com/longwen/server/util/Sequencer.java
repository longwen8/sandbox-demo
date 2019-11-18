package com.longwen.server.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by huangxinping on 19/11/18.
 */
public class Sequencer {

    // 全局序列
    private static final AtomicInteger sequenceRef = new AtomicInteger(1000);

    public int next(){
        return sequenceRef.getAndIncrement();
    }
}
