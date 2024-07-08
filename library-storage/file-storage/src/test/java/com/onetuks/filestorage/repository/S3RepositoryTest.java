package com.onetuks.filestorage.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetuks.filestorage.FileStorageIntegrationTest;
import com.onetuks.libraryobject.ImageFileFixture;
import com.onetuks.libraryobject.enums.ImageType;
import com.onetuks.libraryobject.vo.ImageFile;
import java.io.File;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

class S3RepositoryTest extends FileStorageIntegrationTest {

  @Autowired private S3Repository s3Repository;

  @Test
  @DisplayName("파일이 성공적으로 S3에 업로드된다.")
  void s3PutFileSuccessTest() {
    // Given
    String uuid = UUID.randomUUID().toString();
    ImageFile imageFile = ImageFileFixture.create(ImageType.PROFILE_IMAGE, uuid);

    // When
    s3Repository.putFile(imageFile);

    // Then
    File result = s3Repository.getFile(imageFile);

    assertAll(
        () -> assertThat(result).isFile(),
        () -> assertThat(result).hasSize(imageFile.file().getSize()));
  }

  @Test
  @DisplayName("S3에 있는 파일을 성공적으로 제거한다.")
  void s3DeleteFileSuccessTest() {
    // Given
    String uuid = UUID.randomUUID().toString();
    ImageFile imageFile = ImageFileFixture.create(ImageType.PROFILE_IMAGE, uuid);
    s3Repository.putFile(imageFile);

    // When
    s3Repository.deleteFile(imageFile);

    // Then
    assertThrows(NoSuchKeyException.class, () -> s3Repository.getFile(imageFile));
  }

  @Test
  @DisplayName("s3에 없는 파일을 제거하려고 할때 예외가 발생한다.")
  void S3Delete_NotExistFile_ExceptionTest() {
    // Given
    ImageFile imageFile =
        ImageFileFixture.create(ImageType.PROFILE_IMAGE, UUID.randomUUID().toString());

    // When
    s3Repository.deleteFile(imageFile);

    // Then
    assertThrows(NoSuchKeyException.class, () -> s3Repository.getFile(imageFile));
  }

  @Test
  @DisplayName("기본 파일인 경우 제거하지 않고 조기반환한다.")
  void s3Delete_DefaultFile_EarlyReturnTest() {
    // Given
    ImageFile imageFile =
        ImageFileFixture.create(ImageType.COVER_IMAGE, ImageFile.DEFAULT_COVER_IMAGE_URI);

    // When
    s3Repository.deleteFile(imageFile);

    // Then
    assertThrows(NoSuchKeyException.class, () -> s3Repository.getFile(imageFile));
  }
}
