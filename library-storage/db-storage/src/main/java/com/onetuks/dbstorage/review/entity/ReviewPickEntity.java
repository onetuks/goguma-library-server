package com.onetuks.dbstorage.review.entity;

import com.onetuks.dbstorage.member.entity.MemberEntity;
import com.onetuks.libraryobject.annotation.Generated;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    name = "review_picks",
    uniqueConstraints =
        @UniqueConstraint(
            name = "unq_member_id_review_id",
            columnNames = {"member_id", "review_id"}))
public class ReviewPickEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "review_pick_id", nullable = false)
  private Long reviewPickId;

  @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
  @JoinColumn(name = "member_id", nullable = false)
  private MemberEntity memberEntity;

  @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
  @JoinColumn(name = "review_id", nullable = false)
  private ReviewEntity reviewEntity;

  public ReviewPickEntity(Long reviewPickId, MemberEntity memberEntity, ReviewEntity reviewEntity) {
    this.reviewPickId = reviewPickId;
    this.memberEntity = memberEntity;
    this.reviewEntity = reviewEntity;
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
    ReviewPickEntity that = (ReviewPickEntity) o;
    return Objects.equals(reviewPickId, that.reviewPickId);
  }

  @Override
  @Generated
  public int hashCode() {
    return Objects.hashCode(reviewPickId);
  }
}
