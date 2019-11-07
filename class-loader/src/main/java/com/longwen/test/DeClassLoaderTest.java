package com.longwen.test;

import com.longwen.myloader.DeClassLoader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DeClassLoaderTest {

    public static void main(String[] args) {

        DeClassLoader diskClassLoader = new DeClassLoader("D:\\jar");
        try{
            //必须要将Mytest放在工程以外，不然会首先加载工程内的同名的类文件
            Class c = diskClassLoader.loadClass("com.longwen.test.MyTest");

            if(c != null){
                try {
                    Object obj = c.newInstance();
                    Method method = c.getDeclaredMethod("say",null);
                    method.invoke(obj,null);

                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

        }catch(ClassNotFoundException  e){
            e.printStackTrace();
        }
    }

}
