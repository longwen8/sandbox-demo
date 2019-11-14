package com.longwen.server.app.core.classloader;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class RoutingURLClassLoader extends URLClassLoader {

    private static final Logger logger = LoggerFactory.getLogger(RoutingURLClassLoader.class);
    private final Routing[] routingArray;

    public RoutingURLClassLoader(final URL[] urls,
                                 final Routing... routingArray) {
        super(urls);
        this.routingArray = routingArray;
    }


    public RoutingURLClassLoader(final URL[] urls,
                                 final ClassLoader parent,
                                 final Routing... routingArray) {
        super(urls, parent);
        this.routingArray = routingArray;
    }


    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        return super.loadClass(name, resolve);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }



    /**
     * 类加载路由匹配器
     */
    public static class Routing {

        private final Collection<String/*REGEX*/> regexExpresses = new ArrayList<String>();
        private final ClassLoader classLoader;

        /**
         * 构造类加载路由匹配器
         *
         * @param classLoader       目标ClassLoader
         * @param regexExpressArray 匹配规则表达式数组
         */
        Routing(final ClassLoader classLoader, final String... regexExpressArray) {
            if (ArrayUtils.isNotEmpty(regexExpressArray)) {
                regexExpresses.addAll(Arrays.asList(regexExpressArray));
            }
            this.classLoader = classLoader;
        }

        /**
         * 当前参与匹配的Java类名是否命中路由匹配规则
         * 命中匹配规则的类加载,将会从此ClassLoader中完成对应的加载行为
         *
         * @param javaClassName 参与匹配的Java类名
         * @return true:命中;false:不命中;
         */
        private boolean isHit(final String javaClassName) {
            for (final String regexExpress : regexExpresses) {
                try {
                    if (javaClassName.matches(regexExpress)) {
                        return true;
                    }
                } catch (Throwable cause) {
                    logger.warn("routing {} failed, regex-express={}.", javaClassName, regexExpress, cause);
                }
            }
            return false;
        }

    }

}
