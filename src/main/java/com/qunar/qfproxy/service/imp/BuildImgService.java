package com.qunar.qfproxy.service.imp;

import com.google.common.base.Strings;
import com.qunar.qfproxy.constants.Config;
import com.qunar.qfproxy.controller.NewDownloadController;
import com.qunar.qfproxy.service.IBuildImg;
import com.qunar.qfproxy.utils.Im4JavaUtils;
import org.omg.CORBA.StringSeqHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BuildImgService implements IBuildImg {
    private static final Logger LOGGER = LoggerFactory.getLogger(BuildImgService.class);

    @Override
    public Map<String, Integer> getImgWidthAndHeight(String inputPath) {
        return Im4JavaUtils.getImageInfo(inputPath);
    }

    @Override
    public void buildThumb(String inputPath, String outPath, Integer width, Integer height) {
        if (width == null || width <= 0 || height == null || height <= 0) {
            LOGGER.warn("build thumb fail due to IMG size illegal ");
            return;
        }
        try {
            Map<String, Integer> imgInfo = getImgWidthAndHeight(inputPath);
            Integer originWidth = imgInfo.get("w");
            Integer originHeight = imgInfo.get("h");
            if (originHeight == null || originWidth == null) {
                LOGGER.error("build thumb fail due to get img origin size fail key is{}", inputPath);
                return;
            }
            if (  originWidth<=width && originHeight<=height) {
                LOGGER.info("size  smaller than the limit does not require cutting");
                return;
            }
            Im4JavaUtils.zoomImage(inputPath, outPath, width, height);
        } catch (Exception e) {
            LOGGER.error("build the thumb fail key is {}", inputPath);
            return;
        }

//        double originWiHeRate = 0.0;
//        short longerWiHe = 0; //宽高长度表示 长大于宽1  长小于宽0
//        if(originWidth>originHeight){
//            longerWiHe=0;
//            originWiHeRate = originWidth/originHeight;
//        }
//        if(originWidth<=originHeight){
//            longerWiHe=1;
//            originWiHeRate = originHeight/originWidth;
//        }
//        if(originWiHeRate<= Config.WIDTH_HEIGHT_RATE){
//            return;
//        }
    }

    @Override
    public void buildFuzzy(String inputPath, String outPath, double quality) {
        if (Strings.isNullOrEmpty(inputPath) || Strings.isNullOrEmpty(outPath) || quality <= 0.0 || quality >= 100.0) {
            LOGGER.error("build the fuzzy fail due to the illegal param");
            return;
        }
        Im4JavaUtils.compressImage(inputPath, outPath, quality);
    }
}
