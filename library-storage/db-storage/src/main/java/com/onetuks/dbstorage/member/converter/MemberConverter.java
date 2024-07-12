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

  public MemberEntity toEntity(Member member) {
    return new MemberEntity(
        member.memberId(),
        toEmbeddable(member.authInfo()),
        member.nickname().value(),
        member.introduction(),
        member.interestedCategories(),
        member.isAlarmAccepted(),
        member.points(),
        member.profileImageFile().fileName(),
        member.profileBackgroundImageFile().fileName(),
        toStaticsEntity(member.memberStatics()));
  }

  public Member toDomain(MemberEntity memberEntity) {
    return new Member(
        memberEntity.getMemberId(),
        toValueObject(memberEntity.getAuthInfoEmbeddable()),
        new Nickname(memberEntity.getNickname()),
        memberEntity.getIntroduction(),
        memberEntity.getInterestedCategories(),
        memberEntity.getIsAlarmAccepted(),
        memberEntity.getPoints(),
        ImageFile.of(PROFILE_IMAGE, memberEntity.getProfileImageUri()),
        ImageFile.of(PROFILE_BACKGROUND_IMAGE, memberEntity.getProfileBackgroundImageUri()),
        toStaticsDomain(memberEntity.getMemberStaticsEntity()));
  }

  private AuthInfoEmbeddable toEmbeddable(AuthInfo authInfo) {
    return new AuthInfoEmbeddable(authInfo.socialId(), authInfo.clientProvider(), authInfo.roles());
  }

  private AuthInfo toValueObject(AuthInfoEmbeddable authInfoEmbeddable) {
    return new AuthInfo(
        authInfoEmbeddable.getSocialId(),
        authInfoEmbeddable.getClientProvider(),
        authInfoEmbeddable.getRoles());
  }

  private MemberStaticsEntity toStaticsEntity(MemberStatics memberStatics) {
    return new MemberStaticsEntity(
        memberStatics.memberStaticsId(),
        memberStatics.reviewCounts(),
        memberStatics.followerCounts(),
        memberStatics.followingCounts(),
        memberStatics.reviewCategoryCounts());
  }

  private MemberStatics toStaticsDomain(MemberStaticsEntity memberStaticsEntity) {
    return new MemberStatics(
        memberStaticsEntity.getMemberStaticsId(),
        memberStaticsEntity.getReviewCounts(),
        memberStaticsEntity.getFollowerCounts(),
        memberStaticsEntity.getFollowingCounts(),
        memberStaticsEntity.getReviewCategoryCounts());
  }
}
