package com.longwen.server.app.core.util.matcer.structure;

public interface Access {

    boolean isPublic();

    boolean isPrivate();

    boolean isProtected();

    boolean isStatic();

    boolean isFinal();

    boolean isInterface();

    boolean isNative();

    boolean isAbstract();

    boolean isEnum();

    boolean isAnnotation();

}
