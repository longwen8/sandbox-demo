package com.longwen.server.app.api.filter;

/**
 * 类和方法过滤器
 */
public interface Filter {


    boolean doClassFilter(int access,
                          String javaClassName,
                          String superClassTypeJavaClassName,
                          String [] interfaceTypeJavaClassNameArray,
                          String [] annotationTypeJavaClassNameArray);

    boolean doMethodFilter(int access,
                           String javaMethodName,
                           String[] parameterTypeJavaClassNameArray,
                           String[] throwsTypeJavaClassNameArray,
                           String[] annotationTypeJavaClassNameArray);
}
