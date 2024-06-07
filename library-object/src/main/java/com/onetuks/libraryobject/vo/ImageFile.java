package com.onetuks.libraryobject.vo;

import com.onetuks.libraryobject.enums.ImageType;
import org.springframework.web.multipart.MultipartFile;

public record ImageFile(ImageType imageType, MultipartFile file, String fileName) {

  public static ImageFile of(ImageType imageType, MultipartFile file, String fileName) {
    return new ImageFile(imageType, file, fileName);
  }

  public static ImageFile of(ImageType imageType, String uri) {
    return new ImageFile(imageType, null, uri);
  }

  public String getUri() {
    return this.imageType.getDirectoryPath() + fileName;
  }
}
