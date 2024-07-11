package com.onetuks.dbstorage.book.entity;

import com.onetuks.dbstorage.member.entity.MemberEntity;
import com.onetuks.libraryobject.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

// TODO : memberId, bookId -> 인덱스 설정
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "book_picks")
public class BookPickEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "book_pick_id", nullable = false)
  private Long bookPickId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  private MemberEntity memberEntity;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "book_id", nullable = false)
  private BookEntity bookEntity;

  public BookPickEntity(Long bookPickId, MemberEntity memberEntity, BookEntity bookEntity) {
    this.bookPickId = bookPickId;
    this.memberEntity = memberEntity;
    this.bookEntity = bookEntity;
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
    BookPickEntity that = (BookPickEntity) o;
    return Objects.equals(bookPickId, that.bookPickId);
  }

  @Override
  @Generated
  public int hashCode() {
    return Objects.hashCode(bookPickId);
  }
}
