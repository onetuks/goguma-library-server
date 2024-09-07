package com.onetuks.librarydomain.member.model;

import static com.onetuks.libraryobject.enums.ImageType.PROFILE_BACKGROUND_IMAGE;
import static com.onetuks.libraryobject.enums.ImageType.PROFILE_IMAGE;
import static com.onetuks.libraryobject.vo.ImageFile.DEFAULT_PROFILE_BACKGROUND_IMAGE_URI;
import static com.onetuks.libraryobject.vo.ImageFile.DEFAULT_PROFILE_IMAGE_URI;

import com.onetuks.librarydomain.member.model.vo.AuthInfo;
import com.onetuks.librarydomain.member.model.vo.Nickname;
import com.onetuks.libraryobject.enums.Badge;
import com.onetuks.libraryobject.enums.BadgeType;
import com.onetuks.libraryobject.enums.Category;
import com.onetuks.libraryobject.enums.RoleType;
import com.onetuks.libraryobject.vo.ImageFile;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record Member(
    Long memberId,
    AuthInfo authInfo,
    Nickname nickname,
    String introduction,
    String instagramUrl,
    Set<Category> interestedCategories,
    boolean isAlarmAccepted,
    long points,
    Set<Badge> badges,
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

  public Member changeRoles(Set<RoleType> newRoles) {
    return new Member(
        memberId(),
        new AuthInfo(authInfo.socialId(), authInfo.clientProvider(), newRoles),
        nickname(),
        introduction(),
        instagramUrl(),
        interestedCategories(),
        isAlarmAccepted(),
        points(),
        badges(),
        profileImageFile(),
        profileBackgroundImageFile(),
        memberStatics());
  }

  public Member changeProfile(
      String nickname,
      String introduction,
      String instagramUrl,
      Set<Category> interestedCategories,
      boolean isAlarmAccepted,
      String profileImageFilename,
      String profileBackgroundImageFilename,
      MultipartFile profileImage,
      MultipartFile profileBackgroundImage) {
    return new Member(
        memberId,
        authInfo,
        new Nickname(nickname),
        introduction,
        instagramUrl,
        interestedCategories,
        isAlarmAccepted,
        points,
        badges,
        getProfileImageFile(profileImageFilename, profileImage),
        getProfileBackgroundImageFile(profileBackgroundImageFilename, profileBackgroundImage),
        memberStatics);
  }

  public Member increaseReviewCategoryStatics(Set<Category> categories) {
    return new Member(
        memberId,
        authInfo,
        nickname,
        introduction,
        instagramUrl,
        interestedCategories,
        isAlarmAccepted,
        points,
        badges,
        profileImageFile,
        profileBackgroundImageFile,
        memberStatics.increaseReviewCategoryCounts(categories));
  }

  public Member decreaseReviewCategoryStatics(Set<Category> categories) {
    return new Member(
        memberId,
        authInfo,
        nickname,
        introduction,
        instagramUrl,
        interestedCategories,
        isAlarmAccepted,
        points,
        badges,
        profileImageFile,
        profileBackgroundImageFile,
        memberStatics.decreaseReviewCategoryCounts(categories));
  }

  public Member increaseFollowerCountStatics() {
    return new Member(
        memberId,
        authInfo,
        nickname,
        introduction,
        instagramUrl,
        interestedCategories,
        isAlarmAccepted,
        points,
        badges,
        profileImageFile,
        profileBackgroundImageFile,
        memberStatics.increaseFollowerCount());
  }

  public Member decreaseFollowerCountStatics() {
    return new Member(
        memberId,
        authInfo,
        nickname,
        introduction,
        instagramUrl,
        interestedCategories,
        isAlarmAccepted,
        points,
        badges,
        profileImageFile,
        profileBackgroundImageFile,
        memberStatics.decreaseFollowerCount());
  }

  public Member increaseFollowingCountStatics() {
    return new Member(
        memberId,
        authInfo,
        nickname,
        introduction,
        instagramUrl,
        interestedCategories,
        isAlarmAccepted,
        points,
        badges,
        profileImageFile,
        profileBackgroundImageFile,
        memberStatics.increaseFollowingCount());
  }

  public Member decreaseFollowingCountStatics() {
    return new Member(
        memberId,
        authInfo,
        nickname,
        introduction,
        instagramUrl,
        interestedCategories,
        isAlarmAccepted,
        points,
        badges,
        profileImageFile,
        profileBackgroundImageFile,
        memberStatics.decreaseFollowingCount());
  }

  public Member creditBadgeForReviewRegistration() {
    long reviewCounts = this.memberStatics.reviewCounts();
    Set<Badge> newBadges = Collections.synchronizedSet(new HashSet<>(this.badges));
    Arrays.stream(Badge.values())
        .filter(badge -> badge.getBadgeType() == BadgeType.REVIEW_BADGE)
        .filter(badge -> badge.getBadgeCondition() <= reviewCounts)
        .forEach(newBadges::add);

    return new Member(
        memberId,
        authInfo,
        nickname,
        introduction,
        instagramUrl,
        interestedCategories,
        isAlarmAccepted,
        points,
        newBadges,
        profileImageFile,
        profileBackgroundImageFile,
        memberStatics);
  }

  public Member creditBadgeForFollowerRegistration() {
    long followerCounts = this.memberStatics.followerCounts();
    Set<Badge> newBadges = Collections.synchronizedSet(new HashSet<>(this.badges));
    Arrays.stream(Badge.values())
        .filter(badge -> badge.getBadgeType() == BadgeType.FOLLOWER_BADGE)
        .filter(badge -> badge.getBadgeCondition() <= followerCounts)
        .forEach(newBadges::add);

    return new Member(
        memberId,
        authInfo,
        nickname,
        introduction,
        instagramUrl,
        interestedCategories,
        isAlarmAccepted,
        points,
        newBadges,
        profileImageFile,
        profileBackgroundImageFile,
        memberStatics);
  }

  public Member creditBadgeForAttendance(int attendedCount) {
    Set<Badge> newBadges = Collections.synchronizedSet(new HashSet<>(this.badges));
    Arrays.stream(Badge.values())
        .filter(badge -> badge.getBadgeType() == BadgeType.ATTENDANCE_BADGE)
        .filter(badge -> badge.getBadgeCondition() <= attendedCount)
        .forEach(newBadges::add);

    return new Member(
        memberId,
        authInfo,
        nickname,
        introduction,
        instagramUrl,
        interestedCategories,
        isAlarmAccepted,
        points,
        newBadges,
        profileImageFile,
        profileBackgroundImageFile,
        memberStatics);
  }

  private ImageFile getProfileImageFile(String profileImageFilename, MultipartFile profileImage) {
    return Optional.ofNullable(profileImageFilename)
        .map(
            filename ->
                ImageFile.isDefault(filename)
                    ? ImageFile.of(PROFILE_IMAGE, DEFAULT_PROFILE_IMAGE_URI)
                    : ImageFile.of(PROFILE_IMAGE, profileImage, filename))
        .orElse(profileImageFile);
  }

  private ImageFile getProfileBackgroundImageFile(
      String profileBackgroundImageFilename, MultipartFile profileBackgroundImage) {
    return Optional.ofNullable(profileBackgroundImageFilename)
        .map(
            filename ->
                ImageFile.isDefault(filename)
                    ? ImageFile.of(PROFILE_BACKGROUND_IMAGE, DEFAULT_PROFILE_BACKGROUND_IMAGE_URI)
                    : ImageFile.of(PROFILE_BACKGROUND_IMAGE, profileBackgroundImage, filename))
        .orElse(profileImageFile);
  }
}
