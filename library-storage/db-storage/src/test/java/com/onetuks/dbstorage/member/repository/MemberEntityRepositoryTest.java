package com.onetuks.dbstorage.member.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.dbstorage.DbStorageIntegrationTest;
import com.onetuks.librarydomain.MemberFixture;
import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.libraryobject.enums.ClientProvider;
import com.onetuks.libraryobject.enums.RoleType;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;

class MemberEntityRepositoryTest extends DbStorageIntegrationTest {

  @Test
  @DisplayName("멤버 엔티티를 생성한다.")
  void create() {
    // Given
    Member member = MemberFixture.create(null, RoleType.USER);

    // When
    Member result = memberEntityRepository.create(member);

    // Then
    assertThat(result.memberId()).isPositive();
  }

  @Test
  @DisplayName("멤버 엔티티를 조회한다.")
  void read() {
    // Given
    Member member = memberEntityRepository.create(MemberFixture.create(null, RoleType.USER));

    // When
    Member result = memberEntityRepository.read(member.memberId());

    // Then
    assertAll(
        () -> assertThat(result.nickname().value()).isEqualTo(member.nickname().value()),
        () -> assertThat(result.introduction()).isEqualTo(member.introduction()),
        () -> assertThat(result.authInfo().socialId()).isEqualTo(member.authInfo().socialId()),
        () -> assertThat(result.authInfo().clientProvider()).isEqualTo(member.authInfo().clientProvider())
    );
  }

  @Test
  @DisplayName("소셜 아이디와 소셜 로그인 클라이언트로 멤버 엔티티를 조회한다.")
  void read_WithSocialIdAndClientProvider_Test() {
    // Given
    Member member = memberEntityRepository.create(MemberFixture.create(null, RoleType.USER));

    // When
    Optional<Member> result =
        memberEntityRepository.read(member.authInfo().socialId(), member.authInfo().clientProvider());

    // Then
    assertThat(result).isPresent();
  }

  @Test
  @DisplayName("존재하지 않는 멤버를 소셜 아이디와 소셜 로그인 클라이언트로 조회하면 빈 옵셔널을 반환한다.")
  void read_WithSocialIdAndClientProvider_EmptyTest() {
    // Given
    String notExistsSocialId = "notExistsSocialId";
    ClientProvider notExistsClientProvider = ClientProvider.NAVER;

    // When
    Optional<Member> result = memberEntityRepository.read(notExistsSocialId, notExistsClientProvider);

    // Then
    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("멤버 엔티티를 갱신한다.")
  void update() {
    // Given
    Member member = memberEntityRepository.create(MemberFixture.create(null, RoleType.USER));
    Member expected = MemberFixture.create(member.memberId(), RoleType.ADMIN);

    // When
    Member result = memberEntityRepository.update(expected);

    // Then
    assertAll(
        () -> assertThat(result.memberId()).isEqualTo(expected.memberId()),
        () -> assertThat(result.authInfo().roles()).contains(RoleType.ADMIN),
        () -> assertThat(result.nickname().value()).isEqualTo(expected.nickname().value())
    );
  }

  @Test
  @DisplayName("멤버 엔티티를 제거한다.")
  void delete() {
    // Given
    Member member = memberEntityRepository.create(MemberFixture.create(null, RoleType.USER));

    // When
    memberEntityRepository.delete(member.memberId());

    // Then
    assertThatThrownBy(() -> memberEntityRepository.read(member.memberId()))
        .isInstanceOf(JpaObjectRetrievalFailureException.class);
  }
}