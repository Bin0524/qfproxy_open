package com.qunar.qfproxy.utils;

import com.google.common.base.Strings;
import com.qunar.qfproxy.constants.Config;
import com.qunar.qfproxy.constants.QMonitorConstants;
import com.qunar.qfproxy.constants.StorageConfig;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.qunar.qfproxy.constants.Config.getProperty;

public class webpUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(webpUtils.class);
    private static final String ENCODE_WEBP_FORMAT = "cwebp -q %s %s -o %s";
    private static final String DECODE_WEBP_FORMAT = "dwebp  %s -o %s";

    public static boolean encodeWebpByCmd(String inputJpgPath, String outputJpgPath, String key) throws IOException {
        if(Strings.isNullOrEmpty(inputJpgPath) || Strings.isNullOrEmpty(outputJpgPath)){
            LOGGER.error("convert file path is null");
            return false;
        }
        if(new File(inputJpgPath).exists())
        try {
            long start = System.currentTimeMillis();
            String cmd = String.format(ENCODE_WEBP_FORMAT, Config.WEBP_Q, inputJpgPath, outputJpgPath);
            Process ps = Runtime.getRuntime().exec(cmd);
            ps.waitFor();
            LOGGER.info("WEBP convert key is {} cost {} ms", key, System.currentTimeMillis() - start);
            return true;
        } catch (Exception e) {
            LOGGER.error("convert webp fail key is {}", key, e);
        }
        return false;
    }



    public static boolean decodeWebpByCmd(String inputJpgPath, String outputJpgPath, String key) throws IOException {
        if(Strings.isNullOrEmpty(inputJpgPath) || Strings.isNullOrEmpty(outputJpgPath)){
            LOGGER.error("convert file path is null");
            return false;
        }
        try {
            long start = System.currentTimeMillis();
            String cmd = String.format(DECODE_WEBP_FORMAT, inputJpgPath, outputJpgPath);
            Process ps = Runtime.getRuntime().exec(cmd);
            ps.waitFor();
            LOGGER.info("decode webp  key is {} cost {} ms", key, System.currentTimeMillis() - start);
            return true;
        } catch (Exception e) {
            LOGGER.error("decode webp fail key is {}", key, e);
        }
        return false;
    }


}
