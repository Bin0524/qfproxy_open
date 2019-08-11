package com.qunar.qfproxy.controller;

import com.qunar.qfproxy.constants.Config;
import com.qunar.qfproxy.constants.StorageConfig;
import com.qunar.qfproxy.model.ImgType;
import com.qunar.qfproxy.model.PlatFormType;
import com.qunar.qfproxy.service.DownloadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;


@Controller
@RequestMapping(value = {"/v1", "v2"})
public class NewDownloadController {
    private static final Logger LOGGER = LoggerFactory.getLogger(NewDownloadController.class);

    @Autowired
    private DownloadService downloadService;

    @RequestMapping(value = "/download/{key:.*}", method = RequestMethod.GET)
    public void download(
            @PathVariable(value = "key") String key,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "webp", required = false) boolean webp,
            @RequestParam(value = "webpsou", required = false) boolean webpsou,
            @RequestParam(value = "platform", required = false) String plat,
            @RequestParam(value = "imgtype", required = false) String imgType,
            HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, IOException {

        String fileName = key;
        if (fileName == null || fileName.trim().length() == 0) {
            resp.reset();
            resp.setContentType("text/plain;charset=utf-8");
            PrintWriter writer = resp.getWriter();
            writer.write("error:can't get the file name! 不能获取文件名");
            writer.flush();
            return;
        }
        if(!downloadService.checkImg(key)){
            downloadService.downloadService(StorageConfig.SWIFT_FOLDER+fileName, req, resp);
            return;
        }
        if (webp && webpsou) {
            StringBuffer keyWeb = new StringBuffer();
            keyWeb.append(key.substring(0, key.lastIndexOf("."))).append(".webp");
            LOGGER.info("due to support webp convert key {} to {}", key, keyWeb.toString());
            fileName = keyWeb.toString();
        }
        PlatFormType platFormType = PlatFormType.of(plat);
        ImgType imgType1 = ImgType.of(imgType);
        if (platFormType == null || imgType1 == null) {
            fileName = StorageConfig.SWIFT_FOLDER + fileName;
            downloadService.downloadService(fileName, req, resp);
        }
        Integer imgSize = null;
        String downloadKey;
        String originKey = key.substring(0, key.lastIndexOf("."));
        String originType = key.substring(key.lastIndexOf(".")+1,key.length());
        switch (platFormType) {
            case PC:
                imgSize = Config.PC_THUMB_SIZE;
                break;
            case TOUCH:
                imgSize = Config.TOUCH_THUMB_SIZE;
                break;
        }
        switch (imgType1) {
            case ORIGIN:
                downloadService.downloadService(fileName, req, resp);
                break;
            case THUMB:
                String thumbKey = String.format(Config.THUMB_KEY_FORMAT,originKey,imgSize,imgSize,originType);
                File file = new File(StorageConfig.SWIFT_FOLDER + thumbKey );
                if(file.exists() && file.isFile()) {
                    downloadService.downloadService(StorageConfig.SWIFT_FOLDER + thumbKey, req, resp);
                }else {
                    downloadService.downloadService(StorageConfig.SWIFT_FOLDER +fileName, req, resp);
                }
                break;
            case FUZZY:
                String fuzzyKey = String.format(Config.FUZZY_KEY_FORMAT,originKey,originType);
                File fileFuzzt = new File(StorageConfig.SWIFT_FOLDER + fuzzyKey );
                if(fileFuzzt.exists() && fileFuzzt.isFile()) {
                    downloadService.downloadService(StorageConfig.SWIFT_FOLDER + fuzzyKey, req, resp);
                }else {
                    downloadService.downloadService(StorageConfig.SWIFT_FOLDER +fileName, req, resp);
                }
                break;
            case avatar:
                downloadService.downloadService(fileName, req, resp);
                break;
        }
    }
}
