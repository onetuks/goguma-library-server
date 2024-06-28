package com.onetuks.libraryobject.vo;

import com.onetuks.libraryobject.enums.ImageType;
import org.springframework.web.multipart.MultipartFile;

public record ImageFile(ImageType imageType, MultipartFile file, String fileName) {

  private static final String AWS_BUCKET_URL = "https://test-bucket.com";
  public static final String DEFAULT_PROFILE_IMAGE_URI = "/profiles/default-profile.png";
  public static final String DEFAULT_PROFILE_BACKGROUND_IMAGE_URI =
      "/profile-backgrounds/default-profile-background.png";

  public static ImageFile of(ImageType imageType, MultipartFile file, String uuid) {
    return new ImageFile(imageType, file, uuid);
  }

  public static ImageFile of(ImageType imageType, String uri) {
    return new ImageFile(imageType, null, uri);
  }

  public String getUri() {
    return this.imageType().getDirectoryPath() + fileName();
  }

  public String getUrl() {
    return AWS_BUCKET_URL + getUri();
  }

  public boolean isDefault() {
    return DEFAULT_PROFILE_IMAGE_URI.equals(getUri())
        || DEFAULT_PROFILE_BACKGROUND_IMAGE_URI.equals(getUri());
  }
}
