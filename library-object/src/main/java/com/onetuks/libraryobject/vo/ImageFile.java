package com.onetuks.libraryobject.vo;

import com.onetuks.libraryobject.enums.ImageType;
import org.springframework.web.multipart.MultipartFile;

public record ImageFile(ImageType imageType, MultipartFile file, String fileName) {

  public String getUri() {
    return this.imageType.getDirectoryPath() + fileName;
  }
}
