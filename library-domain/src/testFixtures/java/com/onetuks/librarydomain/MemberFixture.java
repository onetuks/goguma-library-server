package com.onetuks.librarydomain;

import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.librarydomain.member.model.vo.AuthInfo;
import com.onetuks.librarydomain.member.model.vo.Nickname;
import com.onetuks.libraryobject.ImageFileFixture;
import com.onetuks.libraryobject.enums.Category;
import com.onetuks.libraryobject.enums.ClientProvider;
import com.onetuks.libraryobject.enums.ImageType;
import com.onetuks.libraryobject.enums.RoleType;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MemberFixture {

  private static final Random random = new Random();
  private static final List<String> NICKNAMES =
      List.of("빠니보틀", "곽튜브", "침착맨", "허니콤보", "김용명", "궤도", "셜록현준", "조승연", "별별역사");

  public static Member create(Long memberId, RoleType roleType) {
    return new Member(
        memberId,
        createAuthInfo(roleType),
        new Nickname(createNickname()),
        "소개글입니다.",
        createInterestedCategories(),
        createIsAlarmAccepted(),
        createPoints(),
        ImageFileFixture.create(ImageType.PROFILE_IMAGE, UUID.randomUUID().toString()),
        ImageFileFixture.create(ImageType.PROFILE_BACKGROUND_IMAGE, UUID.randomUUID().toString()),
        null);
  }

  public static Member createWithMockFile(Long memberId, RoleType roleType) {
    return new Member(
        memberId,
        createAuthInfo(roleType),
        new Nickname(createNickname()),
        "소개글입니다.",
        createInterestedCategories(),
        createIsAlarmAccepted(),
        createPoints(),
        ImageFileFixture.createMock(ImageType.PROFILE_IMAGE, UUID.randomUUID().toString()),
        ImageFileFixture.createMock(ImageType.PROFILE_BACKGROUND_IMAGE, UUID.randomUUID().toString()),
        null);
  }

  private static AuthInfo createAuthInfo(RoleType roleType) {
    return new AuthInfo(
        UUID.randomUUID().toString(),
        ClientProvider.values()[random.nextInt(ClientProvider.values().length)],
        createRoles(roleType));
  }

  private static Set<RoleType> createRoles(RoleType roleType) {
    if (roleType == RoleType.ADMIN) {
      return Set.of(RoleType.ADMIN, RoleType.USER);
    }
    return Set.of(RoleType.USER);
  }

  private static String createNickname() {
    String postFix = UUID.randomUUID().toString().substring(0, 5).replace("-", "");
    return NICKNAMES.get(random.nextInt(NICKNAMES.size())) + postFix;
  }

  private static Set<Category> createInterestedCategories() {
    return IntStream.range(0, random.nextInt(1, 3))
        .mapToObj(i -> Category.values()[random.nextInt(Category.values().length)])
        .collect(Collectors.toSet());
  }

  private static long createPoints() {
    return random.nextLong(1_000);
  }

  private static boolean createIsAlarmAccepted() {
    return random.nextBoolean();
  }
}
