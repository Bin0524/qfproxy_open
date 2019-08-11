package com.qunar.qfproxy.service;

import com.google.common.base.Strings;
import com.qunar.qfproxy.constants.Config;
import com.qunar.qfproxy.constants.StorageConfig;
import com.qunar.qfproxy.constants.SwitchStatus;
import com.qunar.qfproxy.model.FileType;
import com.qunar.qfproxy.model.UploadInfo;
import com.qunar.qfproxy.service.imp.BuildImgService;
import com.qunar.qfproxy.utils.webpUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

@Service("uploadService")
public class UploadService implements IUploadService {
    private static final String WEBP_SUFFIX = "webp";
    private static final String PNG_SUFFIX = "png";
    private ExecutorService uploadPool = Executors.newFixedThreadPool(200);

    @Resource
    private BuildImgService buildImgService;

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

    public void buildThumbAndFuzzy(String key, String realKey) {
        if (Strings.isNullOrEmpty(key) || Strings.isNullOrEmpty(realKey) || !DownloadService.checkImg(realKey)) {
            return;
        }
        String inputPath = new StringBuffer(StorageConfig.SWIFT_FOLDER).append(key)
                .append(".").append(realKey).toString();
        if (Config.THUMB_SIZE != null && Config.CUT_SWITCH.equalsIgnoreCase(SwitchStatus.ON.getStatus())) {
            uploadPool.execute(new Thread(new Runnable() {
                @Override
                public void run() {
                    Config.THUMB_SIZE.stream().forEach(x -> {
                        String thumbKey = String.format(Config.THUMB_KEY_FORMAT, x, x, realKey);
                        StringBuffer outPath = new StringBuffer(StorageConfig.SWIFT_FOLDER).append(thumbKey);
                        buildImgService.buildThumb(inputPath, outPath.toString(), x, x);
                    });
                    String fuzzyKey = String.format(Config.FUZZY_KEY_FORMAT, key, realKey);
                    StringBuffer fuzzyPath = new StringBuffer(StorageConfig.SWIFT_FOLDER).append(fuzzyKey);
                    buildImgService.buildFuzzy(inputPath, fuzzyPath.toString(), Config.COMPRESS_QUALITY);
                }
            }));


        }


    }
}
