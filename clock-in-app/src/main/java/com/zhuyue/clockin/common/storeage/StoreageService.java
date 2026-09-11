package com.zhuyue.clockin.common.storeage;

import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.util.List;
import com.zhuyue.clockin.common.exception.BusinessException;
import com.zhuyue.clockin.common.constants.ExceptionCodeEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
  * @description 存储服务, 目前主要是静态文件直接写入，支撑头像图片
*/
@Service("StoreageService")
public class StoreageService {
  @Value("${file.uploadPath}")
  private String uploadPath;

  public String uploadImage(MultipartFile file) throws IOException, BusinessException {
    // 获取文件字节数组
    byte[] bytes = file.getBytes();
    // 获取文件名
    String filePath = file.getOriginalFilename();
    // 文件MIME类型
    String mimeType = file.getContentType();

    List<String> mimeTypes = List.of("image/jpeg", "image/png", "image/jpg");

    if (!mimeTypes.contains(mimeType)) {
      // 如果文件类型不支持，则抛出异常
      throw new BusinessException(ExceptionCodeEnum.InvalidMimeType.getCode(), ExceptionCodeEnum.InvalidMimeType.getMessage());
    }

    String storageFilePath = uploadPath + "/images/" + filePath;

    // 写入文件
    OutputStream fileOutput = new FileOutputStream(storageFilePath);

    fileOutput.write(bytes);
    // 关闭文件输出流
    fileOutput.close();

    return storageFilePath;
  }
}
