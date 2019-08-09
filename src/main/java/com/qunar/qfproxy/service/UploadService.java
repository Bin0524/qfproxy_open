package com.qunar.qfproxy.service;

import com.google.common.base.Strings;
import com.qunar.qfproxy.constants.Config;
import com.qunar.qfproxy.constants.StorageConfig;
import com.qunar.qfproxy.constants.SwitchStatus;
import com.qunar.qfproxy.model.FileType;
import com.qunar.qfproxy.model.UploadInfo;
import com.qunar.qfproxy.utils.webpUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeoutException;

@Service("uploadService")
public class UploadService implements IUploadService {
     private static final String WEBP_SUFFIX = "webp";
    private static final String PNG_SUFFIX = "png";
    @Override
    public UploadInfo isFileExist(FileType fileType, String key) throws InterruptedException, TimeoutException {
        return null;
    }

    @Override
    public UploadInfo upload(FileType fileType, String key, InputStream fileIs, String realType) throws InterruptedException, TimeoutException, IOException {
        String newFileName = StorageConfig.SWIFT_FOLDER + key;
        UploadInfo uploadInfo = new UploadInfo();
        File saveFile = new File(newFileName);
        if (fileType.equals(FileType.FILE)) {
            FileUtils.copyInputStreamToFile(fileIs, saveFile);
            uploadInfo.setSucc(true);
            return uploadInfo;
        }
        StringBuffer originPath = new StringBuffer(newFileName);
        StringBuffer webpPath = new StringBuffer(newFileName);
        originPath.append(".").append(realType);
        webpPath.append(".").append(WEBP_SUFFIX);
        FileUtils.copyInputStreamToFile(fileIs, new File(originPath.toString()));
        uploadInfo.setSucc(true);
        System.out.println(Config.TRAN_WEBP_SWITCH);
        if (!Strings.isNullOrEmpty(realType) && !realType.equalsIgnoreCase(WEBP_SUFFIX)
                && SwitchStatus.ON.getStatus().equalsIgnoreCase(Config.TRAN_WEBP_SWITCH)) {
            uploadInfo.setWebpSour(webpUtils.encodeWebpByCmd(originPath.toString(), webpPath.toString(), key));
            return uploadInfo;
        }
        if (!Strings.isNullOrEmpty(realType) && realType.equalsIgnoreCase(WEBP_SUFFIX)) {
            StringBuffer pngPath = new StringBuffer(newFileName).append(".").append(PNG_SUFFIX);
            webpUtils.decodeWebpByCmd(originPath.toString(), pngPath.toString(), key);
            uploadInfo.setWebpSour(true);
        }
        return null;
    }

    @Override
    public boolean delete(FileType fileType, String key) throws InterruptedException, TimeoutException {
        return false;
    }
}
