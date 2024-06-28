package com.onetuks.dbstorage.book.entity;

import com.onetuks.libraryobject.annotation.Generated;
import com.onetuks.libraryobject.enums.Category;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "books")
public class BookEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "book_id", nullable = false)
  private Long bookId;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "author_name", nullable = false)
  private String authorName;

  @Column(name = "introduction")
  private String introduction;

  @Column(name = "isbn")
  private String isbn;

  @Column(name = "publisher")
  private String publisher;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "category")
  private Category category;

  @Column(name = "cover_image_uri")
  private String coverImageUri;

  @Column(name = "is_indie", nullable = false)
  private Boolean isIndie;

  @Column(name = "is_permitted", nullable = false)
  private Boolean isPermitted;

  public BookEntity(
      Long bookId,
      String title,
      String authorName,
      String introduction,
      String isbn,
      String publisher,
      Category category,
      String coverImageUri,
      Boolean isIndie,
      Boolean isPermitted) {
    this.bookId = bookId;
    this.title = title;
    this.authorName = authorName;
    this.introduction = introduction;
    this.isbn = isbn;
    this.publisher = publisher;
    this.category = Objects.requireNonNullElse(category, Category.ETC);
    this.coverImageUri = coverImageUri;
    this.isIndie = Objects.requireNonNullElse(isIndie, false);
    this.isPermitted = Objects.requireNonNullElse(isPermitted, false);
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
    BookEntity that = (BookEntity) o;
    return Objects.equals(bookId, that.bookId);
  }

  @Override
  @Generated
  public int hashCode() {
    return Objects.hashCode(bookId);
  }
}
