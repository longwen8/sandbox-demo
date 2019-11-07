package com.longwen.test;

/**
 * ClassLoader获取测试类
 * 说明三种java基础加载类的层级以及对应加载的路径。
 */
public class ClassLoaderTest {

    public static void main(String[] args) {



        /** 一般的应用程序的加载器都是 AppClassLoader 对应的是程序文件目录或者第三方jar**/
        ClassLoader cl = ClassLoaderTest.class.getClassLoader();
        System.out.println("MyClass ClassLoader is :" + cl);

        /** AppClassLoader之上是 Extention ClassLoader ext 对应的是 jre 的lib/ext目录下，或者tomcat下的lib/ext目录下的 jar**/
        cl = cl.getParent();
        System.out.println("MyClass Parent ClassLoader is :" + cl);

        /** Ext 这之上的就是 Bootstrap ClassLoader了，这个获取为null ,JDK加载逻辑会自动找 java_home的lib下和rt.jar,tools.jar**/
        cl = cl.getParent();
        System.out.println("MyClass Parent Parent ClassLoader is :" + cl);

        /** String 这个是属于最高级的 Bootstrap Classloader **/
        cl = String.class.getClassLoader();
        System.out.println("String ClassLoader is :" + cl);


    }
}
