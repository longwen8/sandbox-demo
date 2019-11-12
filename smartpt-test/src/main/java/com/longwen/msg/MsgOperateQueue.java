package com.longwen.msg;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

public class MsgOperateQueue {

    private static Queue<Msg> msgQueue = new LinkedBlockingDeque<Msg>();

    /**
     * <p>Title: addMsg</p>
     * <p>Description: 添加Msg</p>
     * @param msg msg
     */
    public static void addMsg(Msg msg) {
        msgQueue.add(msg);
    }

    /**
     * <p>Title: removeMsg</p>
     * <p>Description: 移除Msg</p>
     * @return Msg
     */
    public static Msg removeMsg() {
        return msgQueue.remove();
    }

    /**
     * <p>Title: getMsgQueue</p>
     * <p>Description: 获得msgQueue</p>
     * @return Queue<Msg>
     */
    public static Queue<Msg> getMsgQueue() {
        return msgQueue;
    }

    /**
     * <p>Title: setMsgQueue</p>
     * <p>Description: 设置msgQueue</p>
     * @param msgQueue msgQueue
     */
    public static void setMsgQueue(Queue<Msg> msgQueue) {
        MsgOperateQueue.msgQueue = msgQueue;
    }


}
