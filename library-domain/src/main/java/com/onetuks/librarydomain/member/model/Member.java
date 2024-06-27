package com.onetuks.librarydomain.member.model;

import com.onetuks.librarydomain.member.model.vo.AuthInfo;
import com.onetuks.librarydomain.member.model.vo.Nickname;
import com.onetuks.librarydomain.member.service.dto.param.MemberProfileParam;
import com.onetuks.libraryobject.enums.Category;
import com.onetuks.libraryobject.enums.ImageType;
import com.onetuks.libraryobject.vo.ImageFile;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record Member(
    Long memberId,
    AuthInfo authInfo,
    Nickname nickname,
    String introduction,
    List<Category> interestedCategories,
    boolean isAlarmAccepted,
    long points,
    ImageFile profileImageFile,
    ImageFile profileBackgroundImageFile,
    MemberStatics memberStatics) {

  public Member changeProfile(
      MemberProfileParam param, MultipartFile profileImage, MultipartFile profileBackgroundImage) {
    return new Member(
        memberId,
        authInfo,
        new Nickname(param.nickname()),
        param.introduction(),
        param.interestedCategories(),
        param.isAlarmAccepted(),
        points,
        Optional.ofNullable(profileImage)
            .map(
                file ->
                    ImageFile.of(
                        ImageType.PROFILE_IMAGE,
                        file,
                        profileImageFile.isDefault()
                            ? UUID.randomUUID().toString()
                            : profileImageFile.getUri()))
            .orElse(profileImageFile),
        Optional.ofNullable(profileBackgroundImage)
            .map(
                file ->
                    ImageFile.of(
                        ImageType.PROFILE_BACKGROUND_IMAGE,
                        file,
                        profileBackgroundImageFile.isDefault()
                            ? UUID.randomUUID().toString()
                            : profileBackgroundImageFile.getUri()))
            .orElse(profileBackgroundImageFile),
        memberStatics);
  }
}
