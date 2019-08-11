package com.qunar.qfproxy.model;

public enum ImgType {
    ORIGIN("origin"), THUMB("thumb"),FUZZY("fuzzy"),avatar("avatar");
    private String type;

    ImgType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public static ImgType of(String type) {
        for (ImgType imgType : ImgType.values()) {
            if (imgType.type.equalsIgnoreCase(type)) {
                return imgType;
            }
        }
        return null;
    }
}
