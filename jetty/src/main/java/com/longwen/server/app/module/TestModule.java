package com.longwen.server.app.module;

import com.longwen.server.app.api.Command;
import com.longwen.server.app.api.Module;

public class TestModule implements Module {


    @Command("version")
    public void test(){

        System.out.println("Im test");

    }


    @Command("version")
    public void test1(){

        System.out.println("Im test1");
    }


    public void test3(){

        System.out.println("Im test3");
    }

}
