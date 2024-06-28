package com.onetuks.dbstorage.member.entity;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;

import com.onetuks.dbstorage.member.entity.embed.AuthInfoEmbeddable;
import com.onetuks.libraryobject.annotation.Generated;
import com.onetuks.libraryobject.enums.Category;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    name = "members",
    uniqueConstraints =
        @UniqueConstraint(
            name = "unq_social_id_client_provider",
            columnNames = {"social_id", "client_provider"}))
public class MemberEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_id", nullable = false)
  private Long memberId;

  @Embedded private AuthInfoEmbeddable authInfoEmbeddable;

  @Column(name = "nickname", nullable = false)
  private String nickname;

  @Column(name = "introduction")
  private String introduction;

  @Type(JsonType.class)
  @Column(name = "interested_categories")
  private List<Category> interestedCategories;

  @Column(name = "is_alarm_accepted", nullable = false)
  private Boolean isAlarmAccepted;

  @Column(name = "points", nullable = false)
  private Long points;

  @Column(name = "profile_image_uri")
  private String profileImageUri;

  @Column(name = "profile_background_image_uri")
  private String profileBackgroundImageUri;

  @OneToOne(
      fetch = FetchType.LAZY,
      cascade = {PERSIST, REMOVE})
  @JoinColumn(name = "member_statics_id", unique = true, nullable = false)
  private MemberStaticsEntity memberStaticsEntity;

  public MemberEntity(
      Long memberId,
      AuthInfoEmbeddable authInfoEmbeddable,
      String nickname,
      String introduction,
      List<Category> interestedCategories,
      Boolean isAlarmAccepted,
      Long points,
      String profileImageUri,
      String profileBackgroundImageUri,
      MemberStaticsEntity memberStaticsEntity) {
    this.memberId = memberId;
    this.authInfoEmbeddable = authInfoEmbeddable;
    this.nickname = nickname;
    this.introduction = introduction;
    this.interestedCategories = Objects.requireNonNullElse(interestedCategories, List.of());
    this.isAlarmAccepted = Objects.requireNonNullElse(isAlarmAccepted, true);
    this.points = Objects.requireNonNullElse(points, 0L);
    this.profileImageUri = profileImageUri;
    this.profileBackgroundImageUri = profileBackgroundImageUri;
    this.memberStaticsEntity =
        Objects.requireNonNullElse(memberStaticsEntity, MemberStaticsEntity.init());
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
    MemberEntity that = (MemberEntity) o;
    return Objects.equals(memberId, that.memberId);
  }

  @Override
  @Generated
  public int hashCode() {
    return Objects.hashCode(memberId);
  }
}
