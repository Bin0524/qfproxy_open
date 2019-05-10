package com.qunar.qfproxy.service;


import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.io.ByteStreams;
import com.qunar.qfproxy.constants.StorageConfig;
import com.qunar.qfproxy.model.DefaultEmoticonModel;
import com.qunar.qfproxy.model.EmoType;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.util.JSONPObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class EmoDownloadService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmoDownloadService.class);



    private static HashMap<String, DefaultEmoticonModel.FaceId> defaultFace = new HashMap<>();


    @Autowired
    private DownloadService downloadService;

    static {
        try {
            ClassPathResource classPathResource = new ClassPathResource("DefaultEmoticon.json");
            InputStream read = classPathResource.getInputStream();
            String config = new String(ByteStreams.toByteArray(read));
            JSONObject jsonObject = JSONObject.parseObject(config);
            JSONObject facesetting = jsonObject.getJSONObject("FACESETTING");
            String defaultface = facesetting.getString("DEFAULTFACE");
            DefaultEmoticonModel entiy = JSONObject.parseObject(defaultface, DefaultEmoticonModel.class);
            String packageName = entiy.getEmoPackage();
            HashMap<String, DefaultEmoticonModel.FaceId> faceIdHashMap = new HashMap<>();
            List<DefaultEmoticonModel.FaceModel> faceModels = entiy.getFACE();
            if (entiy != null && faceModels != null) {
                for (DefaultEmoticonModel.FaceModel faceModel : faceModels) {
                    String key = packageName + faceModel.getShortcut();
                    DefaultEmoticonModel.FaceId faceId = new DefaultEmoticonModel.FaceId(faceModel.getFILE_ORG(), faceModel.getFILE_FIXED());
                    defaultFace.put(key, faceId);
                }
            }

        } catch (Exception e) {
            LOGGER.error("init default emo face fail ", e);
        }
    }

   public void downloadEmo(String emoPackage,String key, String typeName, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(Strings.isNullOrEmpty(typeName)){
            LOGGER.warn("download face emo fail due type is null");
            return;
        }
       EmoType type = EmoType.typeOf(typeName);
       DefaultEmoticonModel.FaceId faceId = defaultFace.get(key);
       if(faceId == null){
           LOGGER.warn("download face emo fail due to no resource key={},type={}",key,typeName);
           return;
       }
       String position = StorageConfig.SWIFT_FOLDER_EMO_PACKAGE + emoPackage;
       switch (type) {
           case FIXED:
               downloadService.downloadService(position+"/"+faceId.getFixed(),request,response);
               break;
           case ORG:
               downloadService.downloadService(position+"/"+faceId.getOrg(),request,response);
               break;
       }
   }
}
