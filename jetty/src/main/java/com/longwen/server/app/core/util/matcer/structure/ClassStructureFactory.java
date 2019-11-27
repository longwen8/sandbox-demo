package com.longwen.server.app.core.util.matcer.structure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class ClassStructureFactory {

    private static final Logger logger = LoggerFactory.getLogger(ClassStructureFactory.class);


    /**
     * 通过Class类来构造类结构
     *
     * @param clazz 目标Class类
     * @return JDK实现的类结构
     */
    public static ClassStructure createClassStructure(final Class<?> clazz) {
       // return new ClassStructureImplByJDK(clazz);
        return null;
    }

    /**
     * 通过Class类字节流来构造类结构
     *
     * @param classInputStream Class类字节流
     * @param loader           即将装载Class的ClassLoader
     * @return ASM实现的类结构
     */
    public static ClassStructure createClassStructure(final InputStream classInputStream,
                                                      final ClassLoader loader) {
//        try {
//            return new ClassStructureImplByAsm(classInputStream, loader);
//        } catch (IOException cause) {
//            logger.warn("create class structure failed by using ASM, return null. loader={};", loader, cause);
//            return null;
//        }
        return null;
    }

    /**
     * 通过Class类字节数组来构造类结构
     *
     * @param classByteArray Class类字节数组
     * @param loader         即将装载Class的ClassLoader
     * @return ASM实现的类结构
     */
    public static ClassStructure createClassStructure(final byte[] classByteArray,
                                                      final ClassLoader loader) {
        //return new ClassStructureImplByAsm(classByteArray, loader);
        return null;
    }


}
