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
    name = "follow_ships",
    uniqueConstraints =
        @UniqueConstraint(
            name = "unq_follower_id_followee_id",
            columnNames = {"follower_id", "followee_id"}))
public class FollowShipEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "follow_ship_id", nullable = false)
  private Long followShipId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "follower_id", nullable = false)
  private MemberEntity follower;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "followee_id", nullable = false)
  private MemberEntity followee;

  public FollowShipEntity(Long followShipId, MemberEntity follower, MemberEntity followee) {
    this.followShipId = followShipId;
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
    FollowShipEntity that = (FollowShipEntity) o;
    return Objects.equals(followShipId, that.followShipId);
  }

  @Override
  @Generated
  public int hashCode() {
    return Objects.hashCode(followShipId);
  }
}
