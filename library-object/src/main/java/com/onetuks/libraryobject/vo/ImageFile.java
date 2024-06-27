package com.onetuks.libraryobject.vo;

import static com.onetuks.libraryobject.enums.ImageType.PROFILE_IMAGE;

import com.onetuks.libraryobject.enums.ImageType;
import org.springframework.web.multipart.MultipartFile;

public record ImageFile(ImageType imageType, MultipartFile file, String fileName) {

  private static final String AWS_BUCKET_URL = "https://test-bucket.com";
  private static final String DEFAULT_PROFILE_IMAGE_URI = "default-profile.png";
  private static final String DEFAULT_PROFILE_BACKGROUND_IMAGE_URI =
      "default-profile-background.png";

  public static ImageFile of(ImageType imageType, MultipartFile file, String uuid) {
    return new ImageFile(imageType, file, uuid);
  }

  public static ImageFile of(ImageType imageType, String uri) {
    return new ImageFile(imageType, null, uri);
  }

  public static String getDefaultProfileImagUri() {
    return PROFILE_IMAGE.getDirectoryPath() + DEFAULT_PROFILE_IMAGE_URI;
  }

  public static String getDefaultProfileBackgroundImageUri() {
    return PROFILE_IMAGE.getDirectoryPath() + DEFAULT_PROFILE_BACKGROUND_IMAGE_URI;
  }

  public String getUri() {
    return this.imageType().getDirectoryPath() + fileName();
  }

  public String getUrl() {
    return AWS_BUCKET_URL + getUri();
  }

  public boolean isNullFile() {
    return file() == null;
  }
}
