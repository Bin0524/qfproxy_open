package com.qunar.qfproxy.service;

import java.util.Map;

public interface IBuildImg {

    /**
     * 获取图片的宽高
     * @param inputPath
     * @return
     */
    Map<String,Integer> getImgWidthAndHeight(String inputPath);


    /**
     * 构建缩率图
     * @param inputPath
     * @param outPath
     * @param width
     * @param height
     */
     void buildThumb(String inputPath,String outPath,Integer width,Integer height);

    /**
     * 构建模糊图
     * @param inputPath
     * @param outPath
     * @param quality
     */
     void buildFuzzy(String inputPath,String outPath,double quality);




}
