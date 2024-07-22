package com.onetuks.dbstorage.review.entity;

import com.onetuks.dbstorage.book.entity.BookEntity;
import com.onetuks.dbstorage.common.BaseCreatedAndUpdatedEntity;
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

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "reviews")
public class ReviewEntity extends BaseCreatedAndUpdatedEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "review_id", nullable = false)
  private Long reviewId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  private MemberEntity memberEntity;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "book_id", nullable = false)
  private BookEntity bookEntity;

  @Column(name = "review_title", nullable = false)
  private String reviewTitle;

  @Column(name = "review_content", nullable = false)
  private String reviewContent;

  @Column(name = "pick_counts", nullable = false)
  private Long pickCount;

  public ReviewEntity(
      Long reviewId,
      MemberEntity memberEntity,
      BookEntity bookEntity,
      String reviewTitle,
      String reviewContent,
      Long pickCount) {
    this.reviewId = reviewId;
    this.memberEntity = memberEntity;
    this.bookEntity = bookEntity;
    this.reviewTitle = reviewTitle;
    this.reviewContent = reviewContent;
    this.pickCount = pickCount;
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
    ReviewEntity that = (ReviewEntity) o;
    return Objects.equals(reviewId, that.reviewId);
  }

  @Override
  @Generated
  public int hashCode() {
    return Objects.hashCode(reviewId);
  }
}
