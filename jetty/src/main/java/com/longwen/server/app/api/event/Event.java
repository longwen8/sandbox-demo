package com.longwen.server.app.api.event;

public abstract class Event {

    public final Type type;

    protected Event(Type type){
        this.type = type;
    }

    public enum Type {
        BEFORE,
        RETURN,
        THROWS,
        LINE,
        CALL_BEFORE,
        CALL_RETURN,
        CALL_THROWS,
        IMMEDIATELY_RETURN,
        IMMEDIATELY_THROWS,

    }
}
