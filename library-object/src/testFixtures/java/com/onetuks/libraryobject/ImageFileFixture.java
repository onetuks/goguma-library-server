package com.onetuks.libraryobject;

import com.onetuks.libraryobject.enums.ImageType;
import com.onetuks.libraryobject.vo.ImageFile;
import java.util.UUID;

public class ImageFileFixture {

  public static ImageFile create(ImageType imageType, String filename) {
    return new ImageFile(imageType, MultipartFileFixture.create(imageType, filename), filename);
  }

  public static ImageFile createMock(ImageType imageType, String uri) {
    return new ImageFile(imageType, MultipartFileFixture.createMock(imageType, uri), uri);
  }

  public static String createFilename() {
    return UUID.randomUUID() + ".png";
  }
}
