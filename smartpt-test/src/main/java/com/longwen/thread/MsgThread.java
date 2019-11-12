package com.longwen.thread;

import com.longwen.msg.Msg;
import com.longwen.msg.MsgOperateQueue;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MsgThread extends Thread {

    // 短信队列无数据，休眠时间
    private static Long sleepSeconds = 10 * 1000L;
    // 短信队列阀值
    private static Integer size = 0;
    // 线程池大小
//    private static final int POOL_SIZE = 200;
    private static final int POOL_SIZE = 50;
    //线程池队列大小
    private static final int POOL_QUEUE_SIZE = 20000;

    ThreadPoolExecutor pool;


    @Override
    public void run() {
        while (true) {
            System.out.println(
                    "run thread:" + Thread.currentThread() + ",MsgOperateQueue.getMsgQueue().size():"
                            + MsgOperateQueue.getMsgQueue()
                            .size());
            long startTime = 0L;
            while (MsgOperateQueue.getMsgQueue().size() > size) {
                if (pool == null || pool.isTerminated()) {
                    pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(POOL_SIZE);
                    startTime = System.currentTimeMillis();
                }
                while (pool.getQueue().size() < POOL_QUEUE_SIZE) {
                    Callable c = new SendMsgCallable();
                    pool.submit(c);

                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            try {
                Thread.sleep(sleepSeconds);
                if (pool != null && pool.getQueue().size() == 0 && !pool.isTerminated()) {
                    pool.shutdown();
                    // 线程池已关闭
                    while (!pool.awaitTermination(1, TimeUnit.SECONDS)) {
                        System.out.println(
                                "pool.isTerminated():" + pool.isTerminated());
                    }

                    long endTime = System.currentTimeMillis();
                    System.out.println(
                            "pool.isTerminated():" + pool.isTerminated()+",pool.getCompletedTaskCount():"+ pool.getCompletedTaskCount()+ ",send cost:" + (endTime
                                    - startTime) + "ms");
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("e.printStackTrace():" + e.toString());
            }
        }
    }


    class SendMsgCallable implements Callable<Object> {

        public SendMsgCallable() {
        }

        @Override
        public Object call() throws Exception {
            Msg msg = MsgOperateQueue.removeMsg();
            System.out.println(Thread.currentThread().getName()+":发送："+msg.toString());
            return null;
        }
    }
}