package com.onetuks.libraryobject.vo;

import com.onetuks.libraryobject.enums.ImageType;
import org.springframework.web.multipart.MultipartFile;

public record ImageFile(ImageType imageType, MultipartFile file, String fileName) {

  private static final String DEFAULT_PROFILE_IMAGE_URI = "default_profile_image.png";
  private static final String DEFAULT_PROFILE_BACKGROUND_IMAGE_URI = "default-profile-bg.png";

  public static ImageFile of(ImageType imageType, MultipartFile file, String fileName) {
    return new ImageFile(imageType, file, fileName);
  }

  public static ImageFile of(ImageType imageType, String uri) {
    return new ImageFile(imageType, null, uri);
  }

  public static String getDefaultProfileImagUri() {
    return ImageType.PROFILE_IMAGE.getDirectoryPath() + DEFAULT_PROFILE_IMAGE_URI;
  }

  public static String getDefaultProfileBackgroundImageUri() {
    return ImageType.PROFILE_IMAGE.getDirectoryPath() + DEFAULT_PROFILE_BACKGROUND_IMAGE_URI;
  }

  public String getUri() {
    return this.imageType.getDirectoryPath() + fileName;
  }
}
