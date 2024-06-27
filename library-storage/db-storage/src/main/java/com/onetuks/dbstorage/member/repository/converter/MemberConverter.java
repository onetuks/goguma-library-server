package com.onetuks.dbstorage.member.repository.converter;

import com.onetuks.dbstorage.member.entity.MemberEntity;
import com.onetuks.dbstorage.member.entity.MemberStaticsEntity;
import com.onetuks.dbstorage.member.entity.embed.AuthInfoEmbeddable;
import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.librarydomain.member.model.MemberStatics;
import com.onetuks.librarydomain.member.model.vo.AuthInfo;
import com.onetuks.librarydomain.member.model.vo.Nickname;
import com.onetuks.libraryobject.enums.ImageType;
import com.onetuks.libraryobject.vo.ImageFile;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class MemberConverter {

  public MemberEntity toEntity(Member member) {
    return new MemberEntity(
        member.memberId(),
        toEmbeddable(member.authInfo()),
        Optional.ofNullable(member.nickname()).map(Nickname::value).orElse(null),
        member.introduction(),
        member.interestedCategories(),
        member.isAlarmAccepted(),
        member.points(),
        Optional.ofNullable(member.profileImageFile()).map(ImageFile::getUri).orElse(null),
        Optional.ofNullable(member.profileBgImageFile()).map(ImageFile::getUri).orElse(null),
        toStaticsEntity(member.memberStatics()));
  }

  public MemberEntity toEntity(Member member, MemberStaticsEntity memberStaticsEntity) {
    return new MemberEntity(
        member.memberId(),
        toEmbeddable(member.authInfo()),
        member.nickname().value(),
        member.introduction(),
        member.interestedCategories(),
        member.isAlarmAccepted(),
        member.points(),
        member.profileImageFile().getUri(),
        member.profileBgImageFile().getUri(),
        memberStaticsEntity);
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
        ImageFile.of(ImageType.PROFILE_IMAGE, memberEntity.getProfileImgUri()),
        ImageFile.of(ImageType.PROFILE_BACKGROUND_IMAGE, memberEntity.getProfileBgImgUri()),
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
    if (memberStatics == null) {
      return null;
    }

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
