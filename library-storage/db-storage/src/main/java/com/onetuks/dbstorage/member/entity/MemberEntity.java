package com.onetuks.dbstorage.member.entity;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;

import com.onetuks.dbstorage.member.entity.embed.AuthInfoEmbeddable;
import com.onetuks.libraryobject.enums.Category;
import com.onetuks.libraryobject.vo.ImageFile;
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
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "members")
public class MemberEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_id", nullable = false)
  private Long memberId;

  @Embedded private AuthInfoEmbeddable authInfoEmbeddable;

  @Column(name = "nickname")
  private String nickname;

  @Column(name = "introduction")
  private String introduction;

  @Type(JsonType.class)
  @Column(name = "interested_categories")
  private List<Category> interestedCategories;

  @Column(name = "points", nullable = false)
  private Long points;

  @Column(name = "is_alarm_accepted", nullable = false)
  private Boolean isAlarmAccepted;

  @Column(name = "profile_img_uri", nullable = false)
  private String profileImgUri;

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
      Long points,
      Boolean isAlarmAccepted,
      String profileImgUri,
      MemberStaticsEntity memberStaticsEntity) {
    this.memberId = memberId;
    this.authInfoEmbeddable = authInfoEmbeddable;
    this.nickname = nickname;
    this.introduction = introduction;
    this.interestedCategories = Objects.requireNonNullElse(interestedCategories, List.of());
    this.points = Objects.requireNonNullElse(points, 0L);
    this.isAlarmAccepted = Objects.requireNonNullElse(isAlarmAccepted, true);
    this.profileImgUri =
        Objects.requireNonNullElse(profileImgUri, ImageFile.getDefaultProfileImagUri());
    this.memberStaticsEntity =
        Objects.requireNonNullElse(memberStaticsEntity, MemberStaticsEntity.init());
  }
}
