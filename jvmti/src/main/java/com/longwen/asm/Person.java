package com.longwen.asm;

/**
 * Created by huangxinping on 19/11/12.
 */
public class Person {

    private String name = "default";

    public void sayHello(){
        System.out.println("Hello "+ name);
    }

    public static void main(String[] args) {
        new Person().sayHello();
    }
}
