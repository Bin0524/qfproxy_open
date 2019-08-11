package com.qunar.qfproxy.model;

/**
 * 客户端类型 PC跟touch
 */
public enum PlatFormType {
    PC("pc"), TOUCH("touch");
    private String type;

    PlatFormType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public static PlatFormType of(String type) {
        for (PlatFormType platformType : PlatFormType.values()) {
            if (platformType.type.equalsIgnoreCase(type)) {
                return platformType;
            }
        }
        return null;
    }
}
