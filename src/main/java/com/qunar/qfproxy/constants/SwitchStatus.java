package com.qunar.qfproxy.constants;

import com.qunar.qfproxy.model.FileType;

public enum SwitchStatus {
    ON("on"), OFF("off");
    private String status;

    SwitchStatus(String status) {
        this.status = status;
    }


    public String getStatus() {
        return this.status;
    }


    public static SwitchStatus of(String type) {
        for (SwitchStatus switchStatus : SwitchStatus.values()) {
            if (switchStatus.status.equals(type)) {
                return switchStatus;
            }
        }
        return null;
    }
}
