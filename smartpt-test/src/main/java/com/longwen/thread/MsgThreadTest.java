package com.longwen.thread;

import com.longwen.msg.MsgQueueTest;

public class MsgThreadTest {

    public static void main(String[] args) {
        new MsgQueueTest().start();
        new MsgThread().start();

    }
}
