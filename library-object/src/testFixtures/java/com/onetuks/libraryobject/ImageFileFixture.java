package com.onetuks.libraryobject;

import com.onetuks.libraryobject.enums.ImageType;
import com.onetuks.libraryobject.vo.ImageFile;
import com.onetuks.libraryobject.vo.ImageFileUriGenerator;

public class ImageFileFixture {

  public static ImageFile create(ImageType imageType, long id) {
    String uri = ImageFileUriGenerator.generateUri(id);
    return new ImageFile(imageType, MultipartFileFixture.create(imageType, uri), uri);
  }
}
