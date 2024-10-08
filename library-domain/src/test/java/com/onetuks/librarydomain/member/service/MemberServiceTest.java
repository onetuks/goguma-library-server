package com.onetuks.librarydomain.member.service;

import static com.onetuks.libraryobject.enums.RoleType.ADMIN;
import static com.onetuks.libraryobject.enums.RoleType.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;

import com.onetuks.librarydomain.DomainIntegrationTest;
import com.onetuks.librarydomain.MemberFixture;
import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.librarydomain.member.service.dto.param.MemberProfileParam;
import com.onetuks.librarydomain.member.service.dto.result.MemberAuthResult;
import com.onetuks.libraryobject.MultipartFileFixture;
import com.onetuks.libraryobject.enums.Category;
import com.onetuks.libraryobject.enums.ImageType;
import com.onetuks.libraryobject.exception.ApiAccessDeniedException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

class MemberServiceTest extends DomainIntegrationTest {

  @Test
  @DisplayName("존재하지 않는 멤버이면 새로 생성해서 멤버 인증 객체로 반환한다.")
  void registerMemberIfNotExistsTest_NotExist_Test() {
    // Given
    Member member = MemberFixture.create(121L, USER);

    given(memberRepository.read(member.authInfo().socialId(), member.authInfo().clientProvider()))
        .willReturn(Optional.empty());
    given(memberRepository.create(any())).willReturn(member);

    // When
    MemberAuthResult result = memberService.registerIfNotExists(member.authInfo());

    // Then
    assertAll(
        () -> assertThat(result.isNewMember()).isTrue(),
        () -> assertThat(result.memberId()).isEqualTo(member.memberId()),
        () ->
            assertThat(result.roles())
                .containsExactlyInAnyOrderElementsOf(member.authInfo().roles()));
  }

  @Test
  @DisplayName("존재하는 멤버이면 멤버 인증 객체로 반환한다.")
  void registerMemberIfNotExistsTest_Exist_Test() {
    // Given
    Member member = MemberFixture.create(122L, USER);

    given(memberRepository.read(member.authInfo().socialId(), member.authInfo().clientProvider()))
        .willReturn(Optional.of(member));

    // When
    MemberAuthResult result = memberService.registerIfNotExists(member.authInfo());

    // Then
    assertAll(
        () -> assertThat(result.isNewMember()).isFalse(),
        () -> assertThat(result.memberId()).isEqualTo(member.memberId()),
        () ->
            assertThat(result.roles())
                .containsExactlyInAnyOrderElementsOf(member.authInfo().roles()));
  }

  @Test
  @DisplayName("멤버 프로필을 조회한다.")
  void searchTest() {
    // Given
    Member member = MemberFixture.create(123L, USER);

    given(memberRepository.read(member.memberId())).willReturn(member);

    // When
    Member result = memberService.search(member.memberId());

    // Then
    assertThat(result.memberId()).isEqualTo(member.memberId());
  }

  @Test
  @DisplayName("멤버 프로필을 수정한다. 프로필 이미지가 주어지면 저장하고, 기존 이미지를 대체한다.")
  void editProfileTest() {
    // Given
    Member member = MemberFixture.create(123L, USER);
    MultipartFile profileImage =
        MultipartFileFixture.create(ImageType.PROFILE_IMAGE, UUID.randomUUID() + ".png");
    MultipartFile profileBackgroundImage =
        MultipartFileFixture.create(ImageType.PROFILE_BACKGROUND_IMAGE, UUID.randomUUID() + ".png");
    MemberProfileParam param =
        new MemberProfileParam(
            "nickname",
            "introduction",
            "www.instagram.com/onetuks",
            Set.of(Category.CARTOON, Category.NOVEL),
            true,
            Objects.requireNonNull(profileImage).getName(),
            Objects.requireNonNull(profileBackgroundImage).getName());
    Member updatedMember =
        member.changeProfile(
            param.nickname(),
            param.introduction(),
            param.instagramUrl(),
            param.interestedCategories(),
            param.isAlarmAccepted(),
            param.profileImageFilename(),
            param.profileBackgroundImageFilename(),
            profileImage,
            profileBackgroundImage);

    given(memberRepository.read(member.memberId())).willReturn(member);
    given(memberRepository.update(any())).willReturn(updatedMember);

    // When
    Member result =
        memberService.editProfile(
            member.memberId(), member.memberId(), param, profileImage, profileBackgroundImage);

    // Then
    assertAll(
        () -> assertThat(result.nickname().value()).isEqualTo(param.nickname()),
        () -> assertThat(result.introduction()).isEqualTo(param.introduction()),
        () ->
            assertThat(result.interestedCategories())
                .containsExactlyInAnyOrderElementsOf(param.interestedCategories()),
        () -> assertThat(result.isAlarmAccepted()).isEqualTo(param.isAlarmAccepted()),
        () -> assertThat(result.profileImageFile()).isEqualTo(updatedMember.profileImageFile()),
        () ->
            assertThat(result.profileBackgroundImageFile())
                .isEqualTo(updatedMember.profileBackgroundImageFile()));

    verify(fileRepository, times(1)).putFile(updatedMember.profileImageFile());
    verify(fileRepository, times(1)).putFile(updatedMember.profileBackgroundImageFile());
  }

  @Test
  @DisplayName("멤버의 권한을 수정한다.")
  void editAuthorities() {
    // Given
    Member userMember = MemberFixture.create(123L, USER);
    Member adminMember = MemberFixture.create(userMember.memberId(), ADMIN);

    given(memberRepository.read(userMember.memberId())).willReturn(userMember);
    given(memberRepository.update(any(Member.class))).willReturn(adminMember);

    // When
    Member result = memberService.editAuthorities(userMember.memberId(), Set.of(USER, ADMIN));

    // Then
    assertAll(
        () -> assertThat(result.memberId()).isEqualTo(adminMember.memberId()),
        () -> assertThat(result.nickname()).isEqualTo(adminMember.nickname()),
        () -> assertThat(result.authInfo()).isEqualTo(adminMember.authInfo()),
        () -> assertThat(result.authInfo().roles()).contains(ADMIN),
        () -> assertThat(result.points()).isEqualTo(adminMember.points()),
        () -> assertThat(result.isAlarmAccepted()).isEqualTo(adminMember.isAlarmAccepted()),
        () -> assertThat(result.profileImageFile()).isEqualTo(adminMember.profileImageFile()),
        () ->
            assertThat(result.profileBackgroundImageFile())
                .isEqualTo(adminMember.profileBackgroundImageFile()));
  }

  @Test
  @DisplayName("권한이 없는 멤버가 프로필을 수정하려고 하면 예외를 던진다.")
  void editProfileTest_AccessDenied_Test() {
    // Given
    long loginId = 123L;
    long memberId = 124L;

    // When & Then
    assertThatThrownBy(() -> memberService.editProfile(loginId, memberId, null, null, null))
        .isInstanceOf(ApiAccessDeniedException.class);
  }

  @Test
  @DisplayName("멤버를 제거하고, 프로필 이미지 파일을 삭제한다.")
  void removeTest() {
    // Given
    Member member = MemberFixture.create(123L, USER);

    given(memberRepository.read(member.memberId())).willReturn(member);

    // When
    memberService.remove(member.memberId());

    // Then
    verify(fileRepository, times(1)).deleteFile(member.profileImageFile());
    verify(memberRepository, times(1)).delete(member.memberId());
  }
}
