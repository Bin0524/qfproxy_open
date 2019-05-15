package com.qunar.qfproxy.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class DefaultEmoticonModel {


    private String emoPackage;
    private String version;
    private String count;
    private String showall;
    private String line;
    private List<FaceModel> FACE;

    @Data
    public  class FaceModel{
        private String id;
        private String shortcut;
        private String tip;
        private String multiframe;
        private String FILE_ORG;
        private String FILE_FIXED;
    }

    @Data
    @Builder
    public static class FaceId{
        private String org;
        private String fixed;

        public FaceId(String org, String fixed) {
            this.org = org;
            this.fixed = fixed;
        }
    }

}
