package com.qunar.qfproxy.constants;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

public class Config {
    private static Properties props;
    public static final String PROJECT_HOST_AND_PORT = getProperty("project.host.and.port");
    public static final String TRAN_WEBP_SWITCH = getProperty("trans.webp.switch");
    public static final String WEBP_Q = getProperty("webp.q");
    public static final Integer PC_THUMB_SIZE = Integer.valueOf(getProperty("img.thumb.size.touch"));
    public static final Integer TOUCH_THUMB_SIZE = Integer.valueOf(getProperty("img.thumb.size.touch"));
    public static final double WIDTH_HEIGHT_RATE = Double.parseDouble((getProperty("img.width.heigh.rate")));
    //public static  HashSet<Integer> THUMB_SIZE = new HashSet<>(2);
    public static final Double COMPRESS_QUALITY = Double.parseDouble(getProperty("compress.quality"));
    public static final String THUMB_KEY_FORMAT = "%s_thumb_%d*%d.%s"; //缩略图的key格式 32813767261735215612546251_thumb_128*128.png
    public static final String FUZZY_KEY_FORMAT = "%s_fuzzy.%s"; //模糊图的key格式 32813767261735215612546251_fuzzy.png
    public static final String CUT_SWITCH = getProperty("cut.switch");


//    static {
//        if(PC_THUMB_SIZE!=null){
//            THUMB_SIZE.add(200);
//        }
//        if(PC_THUMB_SIZE!=null){
//            THUMB_SIZE.add(200);
//        }
//    }

    private synchronized static void init() {
        if (props != null) {
            return;
        }
        InputStreamReader isr = null;
        try {
            String filename = "qfproxy.properties";
            isr = new InputStreamReader(Config.class.getClassLoader().getResourceAsStream(filename), "UTF-8");
            props = new Properties();

            props.load(isr);
        } catch (IOException e) {
            throw new ExceptionInInitializerError("Initialize the config error!");
        } finally {
            closeStream(isr);
        }
    }

    public static String getProperty(String name) {
        if (props == null) {
            init();
        }
        String val = props.getProperty(name.trim());
        if (val == null) {
            return null;
        } else {
            //去除前后端空格
            return val.trim();
        }
    }

    public static String getProperty(String name, String defaultValue) {
        if (props == null) {
            init();
        }

        String value = getProperty(name);
        if (value == null) {
            value = defaultValue;
        }
        return value.trim();
    }


    public static List<String> getListItem(String item) {
        if (props == null) {
            init();
        }

        List<String> list = new ArrayList<>();
        String value = getProperty(item, "");
        if (value.trim().isEmpty()) {
            return list;
        }

        String sepChar = ",";
        if (value.contains(";")) {
            sepChar = ";";
        }
        String[] sa = value.split(sepChar);
        for (String aSa : sa) {
            list.add(aSa.trim());
        }
        return list;
    }

    private static void closeStream(InputStreamReader is) {
        if (is == null) {
            return;
        }

        try {
            is.close();
        } catch (IOException e) {
            throw new ExceptionInInitializerError("Initialize the config error!");
        }
    }
}
