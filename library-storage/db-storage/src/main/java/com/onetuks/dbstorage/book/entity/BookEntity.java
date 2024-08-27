package com.onetuks.dbstorage.book.entity;

import com.onetuks.dbstorage.common.BaseCreatedEntity;
import com.onetuks.dbstorage.member.entity.MemberEntity;
import com.onetuks.libraryobject.annotation.Generated;
import com.onetuks.libraryobject.enums.Category;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.Objects;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    name = "books",
    uniqueConstraints =
        @UniqueConstraint(
            name = "unq_isbn",
            columnNames = {"isbn"}))
public class BookEntity extends BaseCreatedEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "book_id", nullable = false)
  private Long bookId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  private MemberEntity memberEntity;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "author_name", nullable = false)
  private String authorName;

  @Lob
  @Column(name = "introduction")
  private String introduction;

  @Column(name = "isbn", unique = true)
  private String isbn;

  @Column(name = "publisher")
  private String publisher;

  @Type(JsonType.class)
  @Column(name = "categories", nullable = false)
  private Set<Category> categories;

  @Column(name = "cover_image_uri")
  private String coverImageUri;

  @Column(name = "is_indie", nullable = false)
  private Boolean isIndie;

  @Column(name = "is_permitted", nullable = false)
  private Boolean isPermitted;

  @Column(name = "pick_counts", nullable = false)
  private Long pickCounts;

  public BookEntity(
      Long bookId,
      MemberEntity memberEntity,
      String title,
      String authorName,
      String introduction,
      String isbn,
      String publisher,
      Set<Category> categories,
      String coverImageUri,
      Boolean isIndie,
      Boolean isPermitted,
      Long pickCounts) {
    this.bookId = bookId;
    this.memberEntity = memberEntity;
    this.title = title;
    this.authorName = authorName;
    this.introduction = introduction;
    this.isbn = isbn;
    this.publisher = publisher;
    this.categories = categories;
    this.coverImageUri = coverImageUri;
    this.isIndie = Objects.requireNonNullElse(isIndie, false);
    this.isPermitted = Objects.requireNonNullElse(isPermitted, false);
    this.pickCounts = pickCounts;
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
