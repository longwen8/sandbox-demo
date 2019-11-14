package com.longwen.server.app.api;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(METHOD)
@Retention(RUNTIME)
public @interface Command {

    /**
     * 命令名称
     *
     * @return 命令名称
     */
    String value();

}