package com.onetuks.librarydomain.member.model;

import static com.onetuks.libraryobject.enums.ImageType.PROFILE_BACKGROUND_IMAGE;
import static com.onetuks.libraryobject.enums.ImageType.PROFILE_IMAGE;
import static com.onetuks.libraryobject.vo.ImageFile.DEFAULT_PROFILE_BACKGROUND_IMAGE_URI;
import static com.onetuks.libraryobject.vo.ImageFile.DEFAULT_PROFILE_IMAGE_URI;

import com.onetuks.librarydomain.member.model.vo.AuthInfo;
import com.onetuks.librarydomain.member.model.vo.Nickname;
import com.onetuks.libraryobject.enums.Category;
import com.onetuks.libraryobject.enums.RoleType;
import com.onetuks.libraryobject.vo.ImageFile;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record Member(
    Long memberId,
    AuthInfo authInfo,
    Nickname nickname,
    String introduction,
    Set<Category> interestedCategories,
    boolean isAlarmAccepted,
    long points,
    ImageFile profileImageFile,
    ImageFile profileBackgroundImageFile,
    MemberStatics memberStatics) {

  public Member {
    nickname = Optional.ofNullable(nickname).orElse(Nickname.init());
    interestedCategories = Optional.ofNullable(interestedCategories).orElse(Set.of(Category.ALL));
    profileImageFile =
        Optional.ofNullable(profileImageFile)
            .orElse(ImageFile.of(PROFILE_IMAGE, DEFAULT_PROFILE_IMAGE_URI));
    profileBackgroundImageFile =
        Optional.ofNullable(profileBackgroundImageFile)
            .orElse(ImageFile.of(PROFILE_BACKGROUND_IMAGE, DEFAULT_PROFILE_BACKGROUND_IMAGE_URI));
    memberStatics = Optional.ofNullable(memberStatics).orElse(MemberStatics.init());
  }

  public Member changeRoles(List<RoleType> newRoles) {
    return new Member(
        memberId(),
        AuthInfo.builder()
            .socialId(authInfo.socialId())
            .clientProvider(authInfo.clientProvider())
            .roles(newRoles)
            .build(),
        nickname(),
        introduction(),
        interestedCategories(),
        isAlarmAccepted(),
        points(),
        profileImageFile(),
        profileBackgroundImageFile(),
        memberStatics());
  }

  public Member changeProfile(
      String nickname,
      String introduction,
      Set<Category> interestedCategories,
      boolean isAlarmAccepted,
      MultipartFile profileImage,
      MultipartFile profileBackgroundImage) {
    return new Member(
        memberId,
        authInfo,
        new Nickname(nickname),
        introduction,
        interestedCategories,
        isAlarmAccepted,
        points,
        getProfileImageFile(profileImage),
        getProfileBackgroundImageFile(profileBackgroundImage),
        memberStatics);
  }

  private ImageFile getProfileImageFile(MultipartFile profileImage) {
    return Optional.ofNullable(profileImage)
        .map(
            file ->
                ImageFile.of(
                    PROFILE_IMAGE,
                    file,
                    profileImageFile.isDefault()
                        ? UUID.randomUUID().toString()
                        : profileImageFile.fileName()))
        .orElse(profileImageFile);
  }

  private ImageFile getProfileBackgroundImageFile(MultipartFile profileBackgroundImage) {
    return Optional.ofNullable(profileBackgroundImage)
        .map(
            file ->
                ImageFile.of(
                    PROFILE_BACKGROUND_IMAGE,
                    file,
                    profileBackgroundImageFile.isDefault()
                        ? UUID.randomUUID().toString()
                        : profileBackgroundImageFile.fileName()))
        .orElse(profileBackgroundImageFile);
  }
}
