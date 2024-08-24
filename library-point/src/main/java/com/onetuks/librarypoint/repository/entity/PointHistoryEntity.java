package com.onetuks.librarypoint.repository.entity;

import com.onetuks.dbstorage.common.BaseCreatedEntity;
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
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "point_histories")
public class PointHistoryEntity extends BaseCreatedEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "point_history_id", nullable = false)
  private Long pointHistoryId;

  @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
  @JoinColumn(name = "member_id", nullable = false)
  private MemberEntity memberEntity;

  @Column(name = "activity", nullable = false)
  private String activity;

  @Column(name = "points", nullable = false)
  private Long points;

  public PointHistoryEntity(
      Long pointHistoryId,
      MemberEntity memberEntity,
      String activity,
      Long points) {
    this.pointHistoryId = pointHistoryId;
    this.memberEntity = memberEntity;
    this.activity = activity;
    this.points = points;
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
    PointHistoryEntity that = (PointHistoryEntity) o;
    return Objects.equals(pointHistoryId, that.pointHistoryId);
  }

  @Override
  @Generated
  public int hashCode() {
    return Objects.hashCode(pointHistoryId);
  }
}
