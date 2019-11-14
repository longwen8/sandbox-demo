package com.longwen.test;

public class TestClassLoader {
    public static void main(String[] args) {
        ClassLoader c = TestClassLoader.class.getClassLoader();
        System.out.println("c:"+c);
        ClassLoader c1 = c.getParent();
        System.out.println("c1:"+c1);
        ClassLoader c2 = c1.getParent();
        System.out.println("c2:"+c2);
    }
}
