package com.onetuks.librarydomain.book.model;

import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.libraryobject.enums.Category;
import com.onetuks.libraryobject.enums.ImageType;
import com.onetuks.libraryobject.vo.ImageFile;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import org.springframework.web.multipart.MultipartFile;

public record Book(
    Long bookId,
    Member member,
    String title,
    String authorName,
    String introduction,
    String isbn,
    String publisher,
    Set<Category> categories,
    ImageFile coverImageFile,
    boolean isIndie,
    boolean isPermitted,
    long pickCounts,
    LocalDateTime createdAt) {

  public static Book of(
      Member member,
      String title,
      String authorName,
      String introduction,
      String isbn,
      String publisher,
      Set<Category> categories,
      boolean isIndie,
      String coverImageFilename,
      MultipartFile coverImage) {
    return new Book(
        null,
        member,
        title,
        authorName,
        introduction,
        isbn,
        publisher,
        categories,
        Optional.ofNullable(coverImage)
            .map(file -> ImageFile.of(ImageType.COVER_IMAGE, file, coverImageFilename))
            .orElse(
                coverImageFilename != null
                    ? ImageFile.of(ImageType.COVER_IMAGE, coverImageFilename)
                    : ImageFile.of(ImageType.COVER_IMAGE, ImageFile.DEFAULT_COVER_IMAGE_URI)),
        isIndie,
        false,
        0L,
        LocalDateTime.now());
  }

  public Book changeBookInfo(
      String title,
      String authorName,
      String introduction,
      String isbn,
      String publisher,
      Set<Category> categories,
      boolean isIndie,
      boolean isPermitted,
      String coverImageFilename,
      MultipartFile coverImage) {
    return new Book(
        bookId(),
        member(),
        title,
        authorName,
        introduction,
        isbn,
        publisher,
        categories,
        Optional.ofNullable(coverImageFilename)
            .map(
                filename ->
                    ImageFile.isDefault(filename)
                        ? ImageFile.of(ImageType.COVER_IMAGE, ImageFile.DEFAULT_COVER_IMAGE_URI)
                        : ImageFile.of(ImageType.COVER_IMAGE, coverImage, filename))
            .orElse(coverImageFile()),
        isIndie,
        isPermitted,
        pickCounts(),
        createdAt());
  }
}
