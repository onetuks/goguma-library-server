package com.onetuks.dbstorage.member.converter;

import static com.onetuks.libraryobject.enums.ImageType.PROFILE_BACKGROUND_IMAGE;
import static com.onetuks.libraryobject.enums.ImageType.PROFILE_IMAGE;

import com.onetuks.dbstorage.member.entity.MemberEntity;
import com.onetuks.dbstorage.member.entity.MemberStaticsEntity;
import com.onetuks.dbstorage.member.entity.embed.AuthInfoEmbeddable;
import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.librarydomain.member.model.MemberStatics;
import com.onetuks.librarydomain.member.model.vo.AuthInfo;
import com.onetuks.librarydomain.member.model.vo.Nickname;
import com.onetuks.libraryobject.vo.ImageFile;
import org.springframework.stereotype.Component;

@Component
public class MemberConverter {

  public MemberEntity toEntity(Member model) {
    return new MemberEntity(
        model.memberId(),
        toEmbeddable(model.authInfo()),
        model.nickname().value(),
        model.introduction(),
        model.interestedCategories(),
        model.isAlarmAccepted(),
        model.points(),
        model.profileImageFile().fileName(),
        model.profileBackgroundImageFile().fileName(),
        toStaticsEntity(model.memberStatics()));
  }

  public Member toModel(MemberEntity entity) {
    return new Member(
        entity.getMemberId(),
        toValueObject(entity.getAuthInfoEmbeddable()),
        new Nickname(entity.getNickname()),
        entity.getIntroduction(),
        entity.getInterestedCategories(),
        entity.getIsAlarmAccepted(),
        entity.getPoints(),
        ImageFile.of(PROFILE_IMAGE, entity.getProfileImageUri()),
        ImageFile.of(PROFILE_BACKGROUND_IMAGE, entity.getProfileBackgroundImageUri()),
        toStaticsModel(entity.getMemberStaticsEntity()));
  }

  private AuthInfoEmbeddable toEmbeddable(AuthInfo valueObject) {
    return new AuthInfoEmbeddable(valueObject.socialId(), valueObject.clientProvider(), valueObject.roles());
  }

  private AuthInfo toValueObject(AuthInfoEmbeddable embeddable) {
    return new AuthInfo(
        embeddable.getSocialId(),
        embeddable.getClientProvider(),
        embeddable.getRoles());
  }

  private MemberStaticsEntity toStaticsEntity(MemberStatics staticsModel) {
    return new MemberStaticsEntity(
        staticsModel.memberStaticsId(),
        staticsModel.reviewCounts(),
        staticsModel.followerCounts(),
        staticsModel.followingCounts(),
        staticsModel.reviewCategoryCounts());
  }

  private MemberStatics toStaticsModel(MemberStaticsEntity staticsEntity) {
    return new MemberStatics(
        staticsEntity.getMemberStaticsId(),
        staticsEntity.getReviewCounts(),
        staticsEntity.getFollowerCounts(),
        staticsEntity.getFollowingCounts(),
        staticsEntity.getReviewCategoryCounts());
  }
}
