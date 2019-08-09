package com.qunar.qfproxy.utils;

import org.im4java.core.*;
import org.im4java.process.ArrayListOutputConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Im4JavaUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(Im4JavaUtils.class);

    enum CommandType {

        convert("转换处理"), composite("图片合成"), identify("图片信息"),
        ;
        private String name;

        CommandType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    private static ImageCommand getImageCommand(CommandType command) {
        ImageCommand cmd = null;
        switch (command) {
            case convert:
                cmd = new ConvertCmd();
                break;
            case composite:
                cmd = new CompositeCmd();
                break;
            case identify:
                cmd = new IdentifyCmd();
                break;
        }
        return cmd;
    }

    /**
     * 旋转图片
     *
     * @param srcImagePath 原图片地址
     * @param newImagePath 新图片地址
     * @param degree       旋转角度
     * @return
     */
    public static boolean roateImage(String srcImagePath, String newImagePath, Double degree) {
        try {
            IMOperation op = new IMOperation();
            op.addImage(srcImagePath);
            op.rotate(degree);
            op.addImage(newImagePath);
            ImageCommand convert = getImageCommand(CommandType.convert);
            convert.run(op);
            System.out.println("旋转图片成功");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 裁剪图片
     *
     * @param srcImagePath 原图片地址
     * @param newImagePath 新图片地址
     * @param width        裁剪后的宽度
     * @param height       裁剪后的高度
     * @param x            起始横坐标
     * @param y            起始纵坐标
     * @return
     */
    public static boolean cutImage(String srcImagePath, String newImagePath, Integer width, Integer height, Integer x, Integer y) {
        try {
            IMOperation op = new IMOperation();
            op.addImage(srcImagePath);
            op.crop(width, height, x, y);
            op.addImage(newImagePath);
            ImageCommand convert = getImageCommand(CommandType.convert);
            convert.run(op);
            LOGGER.info("裁剪图片成功");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 缩放图片
     *
     * @param srcImagePath 原图片地址
     * @param newImagePath 新图片地址
     * @param width        缩放后的宽度
     * @param height       缩放后的高度
     *                     高度和宽度需符合图片比例，不符合的情况下，以其中最小值为准。
     * @return
     */
    public static boolean zoomImage(String srcImagePath, String newImagePath, Integer width, Integer height) {
        try {
            IMOperation op = new IMOperation();
            op.addImage(srcImagePath);
            op.resize(width, height);
            op.addImage(newImagePath);
            ImageCommand convert = getImageCommand(CommandType.convert);
            convert.run(op);
            LOGGER.info("缩放图片成功");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 压缩图片
     *
     * @param srcImagePath 原图片地址
     * @param newImagePath 新图片地址
     * @param quality      压缩比例
     *                     图片压缩比，有效值范围是0.0-100.0，数值越大，缩略图越清晰。
     * @return
     */
    public static boolean compressImage(String srcImagePath, String newImagePath, Double quality) {
        try {
            IMOperation op = new IMOperation();
            op.addImage(srcImagePath);
            op.quality(quality);
            op.addImage(newImagePath);
            ImageCommand convert = getImageCommand(CommandType.convert);
            convert.run(op);
            LOGGER.info("压缩图片成功");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 文字水印
     *
     * @param srcImagePath 原图片地址
     * @param newImagePath 新图片地址
     * @param content      水印文字，不支持汉字
     * @return
     */
    public static boolean textImage(String srcImagePath, String newImagePath, String content) {
        try {
            IMOperation op = new IMOperation();
            op.addImage(srcImagePath);
            //右下角
            op.font("ArialBold").gravity("southeast").pointsize(60).fill("#F2F2F2").draw("text 10,10 " + content);
            op.addImage(newImagePath);
            ImageCommand convert = getImageCommand(CommandType.convert);
            convert.run(op);
            LOGGER.info("添加文字水印成功");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 图片水印
     *
     * @param srcImagePath    原图片地址
     * @param newImagePath    新图片地址
     * @param appendImagePath 水印图片地址
     * @param dissolve        水印透明度，0-100
     * @return
     */
    public static boolean compositeImage(String srcImagePath, String newImagePath, String appendImagePath, Integer dissolve) {
        try {
            //原图片信息
            BufferedImage bufferedImage = ImageIO.read(new File(srcImagePath));
            int w = bufferedImage.getWidth();
            int h = bufferedImage.getHeight();
            //水印图片信息
            Image image = ImageIO.read(new File(appendImagePath));
            int w1 = image.getWidth(null);
            int h1 = image.getHeight(null);

            IMOperation op = new IMOperation();
            op.geometry(w1, h1, w - w1 - 10, h - h1 - 10);
            op.dissolve(dissolve);
            op.addImage(appendImagePath);
            op.addImage(srcImagePath);
            op.addImage(newImagePath);
            ImageCommand composite = getImageCommand(CommandType.composite);
            composite.run(op);
            LOGGER.info("添加图片水印成功");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 获取图片信息
     *
     * @param imagePath 图片地址
     * @return
     */
    public static Map<String, String> getImageInfo(String imagePath) {
        Map<String, String> imageInfo = new HashMap<>();
        try {
            IMOperation op = new IMOperation();
            op.format("%w,%h");
            op.addImage();
            ImageCommand identifyCmd = getImageCommand(CommandType.identify);//注意这里，用的是identify
            ArrayListOutputConsumer output = new ArrayListOutputConsumer();
            identifyCmd.setOutputConsumer(output);
            identifyCmd.run(op, imagePath);
            ArrayList<String> cmdOutput = output.getOutput();
            String[] result = cmdOutput.get(0).split(",");
            if (result.length == 2) {
                imageInfo.put("w", result[0]);
                imageInfo.put("h", result[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageInfo;
    }

}