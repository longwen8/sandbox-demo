package com.longwen.server.test;

import com.longwen.server.app.api.Command;
import com.longwen.server.app.module.TestModule;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class MethodTest {

    public static void main(String[] args) {
       List<Method> methodList = MethodUtils.getMethodsListWithAnnotation(TestModule.class, Command.class);
       for(Method method : methodList){
           try {
               TestModule testModule = new TestModule();
               // 第一个参数需要是 实例对象
               method.invoke(testModule,null);
           } catch (IllegalAccessException e) {
               e.printStackTrace();
           } catch (InvocationTargetException e) {
               e.printStackTrace();
           }
           System.out.println("method name is --->"+method.getName());
       }

    }
}
