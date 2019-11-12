package com.longwen.msg;



import java.util.Random;

public class MsgQueueTest extends Thread{
    Random ra =new Random();
    int count = 0;

    public static void main(String[] args) {


        new MsgQueueTest().start();


    }

    private void testRemove(){
        Msg msg1 = new Msg();
        msg1.setPhone("15101192563");
        msg1.setMsgType("0");
        msg1.setMsg("aaabbbccceeeffffff");


        Msg msg2 = new Msg();
        msg2.setPhone("122222222222");
        msg2.setMsgType("0");
        msg2.setMsg("cccccccccccc");

        MsgOperateQueue.addMsg(msg1);
        MsgOperateQueue.addMsg(msg2);

        Msg msg = MsgOperateQueue.removeMsg();//先进先出的方式
        System.out.println(msg.toString());
        msg = MsgOperateQueue.removeMsg();
        System.out.println(msg.toString());
    }


    @Override
    public void run() {
      //  while (true){
            for(int i=0;i<5000000;i++){
                Msg msg1 = new Msg();
                count++;
                msg1.setPhone("15"+ra.nextInt(10000000));
                msg1.setMsgType("0");
                msg1.setMsg(count+"----"+ra.nextInt(10000000));
                System.out.println("add msg:"+msg1.toString());
                MsgOperateQueue.addMsg(msg1);
            }
//            try {
//                Thread.sleep(1000L);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
      //  }


    }
}
