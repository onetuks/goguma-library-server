package com.onetuks.libraryobject.vo;

import com.onetuks.libraryobject.annotation.Generated;
import com.onetuks.libraryobject.enums.ImageType;
import java.util.Objects;
import org.springframework.web.multipart.MultipartFile;

public record ImageFile(ImageType imageType, MultipartFile file, String fileName) {

  private static final String AWS_BUCKET_URL =
      "https://goguma-chimpanzees.s3.ap-northeast-2.amazonaws.com";
  public static final String DEFAULT_PROFILE_IMAGE_URI = "default-profile.png";
  public static final String DEFAULT_PROFILE_BACKGROUND_IMAGE_URI =
      "default-profile-background.png";
  public static final String DEFAULT_COVER_IMAGE_URI = "default-cover.png";

  public static ImageFile of(ImageType imageType, MultipartFile file, String fileName) {
    return new ImageFile(imageType, file, fileName);
  }

  public static ImageFile of(ImageType imageType, String uri) {
    return new ImageFile(imageType, null, uri);
  }

  public static boolean isDefault(String fileName) {
    boolean isDefaultProfileImage = fileName.equals(DEFAULT_PROFILE_IMAGE_URI);
    boolean isDefaultProfileBackgroundImage = fileName.equals(DEFAULT_PROFILE_BACKGROUND_IMAGE_URI);
    boolean isDefaultCoverImage = fileName.equals(DEFAULT_COVER_IMAGE_URI);
    return isDefaultProfileImage || isDefaultProfileBackgroundImage || isDefaultCoverImage;
  }

  public String getKey() {
    if (isExternalUrl()) {
      return fileName();
    }
    return this.imageType().getDirectoryPath() + "/" + fileName();
  }

  public String getUrl() {
    if (isExternalUrl()) {
      return fileName();
    }
    return AWS_BUCKET_URL + "/" + getKey();
  }

  private boolean isExternalUrl() {
    return fileName().contains("https://");
  }

  @Override
  @Generated
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImageFile imageFile = (ImageFile) o;
    return Objects.equals(fileName, imageFile.fileName) && imageType == imageFile.imageType;
  }

  @Override
  @Generated
  public int hashCode() {
    return Objects.hash(imageType, fileName);
  }
}
