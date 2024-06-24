package com.onetuks.librarydomain;

import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.librarydomain.member.model.vo.AuthInfo;
import com.onetuks.librarydomain.member.model.vo.Nickname;
import com.onetuks.libraryobject.ImageFileFixture;
import com.onetuks.libraryobject.enums.Category;
import com.onetuks.libraryobject.enums.ClientProvider;
import com.onetuks.libraryobject.enums.ImageType;
import com.onetuks.libraryobject.enums.RoleType;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class MemberFixture {

  private static final Random random = new Random();
  private static final List<String> nicknames =
      List.of("빠니보틀", "곽튜브", "침착맨", "허니콤보", "김용명", "궤도", "셜록현준", "조승연", "별별역사");

  public static Member create(Long memberId, RoleType roleType) {
    return new Member(
        memberId,
        createAuthInfo(roleType),
        new Nickname(createNickname()),
        "소개글입니다.",
        createCategories(),
        createPoints(),
        createIsAlarmAccepted(),
        ImageFileFixture.create(ImageType.PROFILE_IMAGE, UUID.randomUUID().toString()),
        null);
  }

  private static AuthInfo createAuthInfo(RoleType roleType) {
    return new AuthInfo("socialId", ClientProvider.GOOGLE, createRoles(roleType));
  }

  private static List<RoleType> createRoles(RoleType roleType) {
    if (roleType == RoleType.ADMIN) {
      return List.of(RoleType.ADMIN, RoleType.USER);
    }
    return List.of(RoleType.USER);
  }

  private static String createNickname() {
    String postFix = UUID.randomUUID().toString().substring(0, 5).replace("-", "");
    return nicknames.get(random.nextInt(nicknames.size())) + postFix;
  }

  private static List<Category> createCategories() {
    int count = random.nextInt(Category.values().length);
    return Arrays.stream(Category.values(), 0, count).toList();
  }

  private static long createPoints() {
    return random.nextLong(1_000);
  }

  private static boolean createIsAlarmAccepted() {
    return random.nextBoolean();
  }
}
