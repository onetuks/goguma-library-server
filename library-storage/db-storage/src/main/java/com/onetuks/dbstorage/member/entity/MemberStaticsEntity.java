package com.onetuks.dbstorage.member.entity;

import com.onetuks.libraryobject.enums.Category;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "member_statics")
public class MemberStaticsEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_statics_id", nullable = false)
  private Long memberStaticsId;

  @Column(name = "review_counts", nullable = false)
  private Long reviewCounts;

  @Column(name = "follower_counts", nullable = false)
  private Long followerCounts;

  @Column(name = "following_counts", nullable = false)
  private Long followingCounts;

  @Type(JsonType.class)
  @Column(name = "review_category_counts", nullable = false)
  private Map<Category, Long> reviewCategoryCounts;

  public MemberStaticsEntity(
      Long memberStaticsId,
      Long reviewCounts,
      Long followerCounts,
      Long followingCounts,
      Map<Category, Long> reviewCategoryCounts) {
    this.memberStaticsId = memberStaticsId;
    this.reviewCounts = reviewCounts;
    this.followerCounts = followerCounts;
    this.followingCounts = followingCounts;
    this.reviewCategoryCounts = reviewCategoryCounts;
  }
}
