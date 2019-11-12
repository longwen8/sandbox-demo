package com.longwen.instrumentation.agentmain;

public class TargetJVM {

    public static void main(String[] args) throws InterruptedException {
        while (true){
            Thread.sleep(1000);
            System.out.println("Thread sleep ...");

        }
    }
}
