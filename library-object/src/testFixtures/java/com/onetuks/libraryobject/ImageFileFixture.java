package com.onetuks.libraryobject;

import com.onetuks.libraryobject.enums.ImageType;
import com.onetuks.libraryobject.vo.ImageFile;

public class ImageFileFixture {

  public static ImageFile create(ImageType imageType, String uuid) {
    return new ImageFile(imageType, MultipartFileFixture.create(imageType, uuid), uuid);
  }
}
