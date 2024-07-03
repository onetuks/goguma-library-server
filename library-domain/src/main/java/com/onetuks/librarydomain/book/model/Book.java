package com.onetuks.librarydomain.book.model;

import com.onetuks.libraryobject.enums.Category;
import com.onetuks.libraryobject.enums.ImageType;
import com.onetuks.libraryobject.vo.ImageFile;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public record Book(
    Long bookId,
    String title,
    String authorName,
    String introduction,
    String isbn,
    String publisher,
    List<Category> categories,
    ImageFile coverImageFile,
    boolean isIndie,
    boolean isPermitted) {

  public static Book of(
      String title,
      String authorName,
      String isbn,
      String publisher,
      List<Category> categories,
      boolean isIndie,
      MultipartFile coverImage) {
    return new Book(
        null,
        title,
        authorName,
        null,
        isbn,
        publisher,
        categories,
        Optional.ofNullable(coverImage)
            .map(file -> ImageFile.of(ImageType.COVER_IMAGE, file, UUID.randomUUID().toString()))
            .orElse(ImageFile.of(ImageType.COVER_IMAGE, ImageFile.DEFAULT_COVER_IMAGE_URI)),
        isIndie,
        false);
  }
}
