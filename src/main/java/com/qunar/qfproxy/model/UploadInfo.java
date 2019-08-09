package com.qunar.qfproxy.model;

import org.apache.commons.lang3.StringUtils;

/**
 * Author : mingxing.shao
 * Date : 15-12-29
 * email : mingxing.shao@qunar.com
 */
public class UploadInfo {
    private StorageLoc storageLoc;
    private boolean isSucc;
    private String uri;
    private boolean webpSour=false;

    public StorageLoc getStorageLoc() {
        return storageLoc;
    }

    public void setStorageLoc(StorageLoc storageLoc) {
        this.storageLoc = storageLoc;
    }

    public boolean isSucc() {
        return isSucc;
    }

    public void setSucc(boolean succ) {
        isSucc = succ;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setWebpSour(boolean webpSour) {
        this.webpSour = webpSour;
    }

    public boolean isWebpSour() {
        return webpSour;
    }

    public enum StorageLoc {
        SWIFT_PERM_AVATAR_NEW("avatar/new"), SWIFT_PERM_FILE_NEW("perm/new"), SWIFT_TEMP_NEW("temp/new"), SWIFT_PERM_AVATAR("avatar"), SWIFT_PERM_FILE("perm"), SWIFT_TEMP("temp"), BEANS_DB("b");
        private String desc;

        StorageLoc(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }

        public static StorageLoc of(String desc) {
            if (StringUtils.isEmpty(desc)) {
                return null;
            }

            for (StorageLoc storageLoc : StorageLoc.values()) {
                if (storageLoc.desc.equals(desc)) {
                    return storageLoc;
                }
            }
            return null;
        }
    }
}
