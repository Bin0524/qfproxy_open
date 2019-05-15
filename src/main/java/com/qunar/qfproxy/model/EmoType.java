package com.qunar.qfproxy.model;

import java.util.Arrays;

public enum EmoType {

    FIXED("fixed","fixed"),
    ORG("org","org");

    private String type;
    private String desc;

    EmoType(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }


    public String getDesc() {
        return desc;
    }


    public static EmoType typeOf(String type) {
        return Arrays.stream(EmoType.values()).filter(EmoType -> EmoType.getType().equals(type)).findAny().orElse(null);
    }
}
