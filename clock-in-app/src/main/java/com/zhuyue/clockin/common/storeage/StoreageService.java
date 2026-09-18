package com.zhuyue.clockin.common.storeage;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import com.zhuyue.clockin.common.exception.BusinessException;
import com.zhuyue.clockin.common.constants.ExceptionCodeEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
  * @description 存储服务, 目前主要是静态文件直接写入，支撑头像图片
*/
@Service("StoreageService")
public class StoreageService {
  @Value("${file.accessPrefix}")
  private String ACCESS_PREFIX;


  public String uploadImage(MultipartFile file) throws IOException, BusinessException {
    String mimeType = file.getContentType();
    List<String> mimeTypes = List.of("image/jpeg", "image/png", "image/jpg");

    if (!mimeTypes.contains(mimeType)) {
      throw new BusinessException(ExceptionCodeEnum.InvalidMimeType.getCode(), ExceptionCodeEnum.InvalidMimeType.getMessage());
    }

    String originalFilename = file.getOriginalFilename();
    String ext = "";

    if (originalFilename != null && originalFilename.contains(".")) {
      ext = originalFilename.substring(originalFilename.lastIndexOf('.'));
    }

    // 相对路径：入库 / 返回给前端用，不带本地盘符
    String relativePath = "images/" + UUID.randomUUID() + ext;

    // 绝对路径：真正写磁盘
    Path absolutePath = Paths.get("./" + ACCESS_PREFIX, relativePath).toAbsolutePath().normalize();
    Files.createDirectories(absolutePath.getParent());
    Files.write(absolutePath, file.getBytes());

    // 返回可直接访问的相对 URL，例如 /static/images/xxx.png
    return relativePath;
  }
}