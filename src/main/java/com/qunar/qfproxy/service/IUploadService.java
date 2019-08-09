package com.qunar.qfproxy.service;

import com.qunar.qfproxy.model.FileType;
import com.qunar.qfproxy.model.UploadInfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeoutException;

/**
 * Author : mingxing.shao
 * Date : 15-11-3
 * email : mingxing.shao@qunar.com
 */
public interface IUploadService {

    /**
     * 文件是否存在
     *
     * @param fileType 文件类型
     * @param key      文件的key
     * @return 文件上传详情对象(并没有进行任何上传, 但是如果文件存在, 这个和文件上传成功的效果是一样的)
     * @throws InterruptedException .
     * @throws TimeoutException     .
     */
    UploadInfo isFileExist(FileType fileType, String key) throws InterruptedException, TimeoutException;

    /**
     * 文件上传
     *
     * @param fileType 文件类型
     * @param key      文件的key
     * @param fileIs   文件流
     * @return 文件上传结果详情对象
     * @throws InterruptedException .
     * @throws TimeoutException     .
     * @throws IOException          .
     */
    UploadInfo upload(FileType fileType, String key, InputStream fileIs,String realType) throws InterruptedException, TimeoutException, IOException;

    /**
     * 删除文件
     *
     * @param fileType 文件类型
     * @param key      文件的key
     * @return 是否删除成功
     * @throws InterruptedException .
     * @throws TimeoutException     .
     */
    boolean delete(FileType fileType, String key) throws InterruptedException, TimeoutException;
}
