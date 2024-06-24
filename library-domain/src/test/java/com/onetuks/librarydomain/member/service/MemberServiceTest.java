package com.onetuks.librarydomain.member.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.onetuks.librarydomain.DomainIntegrationTest;
import com.onetuks.librarydomain.MemberFixture;
import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.librarydomain.member.service.dto.result.MemberAuthResult;
import com.onetuks.libraryobject.enums.RoleType;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberServiceTest extends DomainIntegrationTest {

  @Test
  @DisplayName("존재하지 않는 멤버이면 새로 생성해서 멤버 인증 객체로 반환한다.")
  void createMemberIfNotExistsTest_NotExistMember_Test() {
    // Given
    Member member = MemberFixture.create(121L, RoleType.USER);

    given(memberRepository.read(member.authInfo().socialId(), member.authInfo().clientProvider()))
        .willReturn(Optional.empty());
    given(memberRepository.create(any())).willReturn(member);

    // When
    MemberAuthResult result = memberService.createMemberIfNotExists(member.authInfo());

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
  void createMemberIfNotExistsTest_ExistMember_Test() {
    // Given
    Member member = MemberFixture.create(122L, RoleType.USER);

    given(memberRepository.read(member.authInfo().socialId(), member.authInfo().clientProvider()))
        .willReturn(Optional.of(member));

    // When
    MemberAuthResult result = memberService.createMemberIfNotExists(member.authInfo());

    // Then
    assertAll(
        () -> assertThat(result.isNewMember()).isFalse(),
        () -> assertThat(result.memberId()).isEqualTo(member.memberId()),
        () ->
            assertThat(result.roles())
                .containsExactlyInAnyOrderElementsOf(member.authInfo().roles()));
  }

  @Test
  @DisplayName("멤버를 제거하고, 프로필 이미지 파일을 삭제한다.")
  void deleteMemberTest() {
    // Given
    Member member = MemberFixture.create(123L, RoleType.USER);

    given(memberRepository.read(member.memberId())).willReturn(member);

    // When
    memberService.deleteMember(member.memberId());

    // Then
    verify(fileRepository, times(1)).deleteFile(member.profileImageFile().getUri());
    verify(memberRepository, times(1)).delete(member.memberId());
  }
}
