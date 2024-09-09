package com.onetuks.dbstorage.member.entity;

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
import jakarta.persistence.UniqueConstraint;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    name = "follows",
    uniqueConstraints =
        @UniqueConstraint(
            name = "unq_follower_id_followee_id",
            columnNames = {"follower_id", "followee_id"}))
public class FollowEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "follow_id", nullable = false)
  private Long followId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "follower_id", referencedColumnName = "member_id", nullable = false)
  private MemberEntity follower;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "followee_id", referencedColumnName = "member_id", nullable = false)
  private MemberEntity followee;

  public FollowEntity(Long followId, MemberEntity follower, MemberEntity followee) {
    this.followId = followId;
    this.follower = follower;
    this.followee = followee;
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
    FollowEntity that = (FollowEntity) o;
    return Objects.equals(followId, that.followId);
  }

  @Override
  @Generated
  public int hashCode() {
    return Objects.hashCode(followId);
  }
}
