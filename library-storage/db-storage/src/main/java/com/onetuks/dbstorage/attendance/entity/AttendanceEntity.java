package com.onetuks.dbstorage.attendance.entity;

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
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    name = "attendances",
    uniqueConstraints =
        @UniqueConstraint(
            name = "unq_member_id_attended_at",
            columnNames = {"member_id", "attended_at"}))
public class AttendanceEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "attendance_id", nullable = false)
  private Long attendanceId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  private MemberEntity memberEntity;

  @Temporal(TemporalType.DATE)
  @Column(name = "attended_at", nullable = false)
  private LocalDate attendedAt;

  @PrePersist
  protected void onPrePersist() {
    this.attendedAt = Objects.requireNonNullElse(attendedAt, LocalDate.now());
  }

  public AttendanceEntity(Long attendanceId, MemberEntity memberEntity) {
    this.attendanceId = attendanceId;
    this.memberEntity = memberEntity;
  }

  public AttendanceEntity(Long attendanceId, MemberEntity memberEntity, LocalDate attendedAt) {
    this.attendanceId = attendanceId;
    this.memberEntity = memberEntity;
    this.attendedAt = attendedAt;
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
    AttendanceEntity that = (AttendanceEntity) o;
    return Objects.equals(attendanceId, that.attendanceId);
  }

  @Override
  @Generated
  public int hashCode() {
    return Objects.hashCode(attendanceId);
  }
}
