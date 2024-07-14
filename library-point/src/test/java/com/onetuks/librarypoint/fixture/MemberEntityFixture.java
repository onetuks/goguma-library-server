package com.onetuks.librarypoint.fixture;

import com.onetuks.dbstorage.member.entity.MemberEntity;
import com.onetuks.dbstorage.member.entity.MemberStaticsEntity;
import com.onetuks.dbstorage.member.entity.embed.AuthInfoEmbeddable;
import com.onetuks.libraryobject.enums.Category;
import com.onetuks.libraryobject.enums.ClientProvider;
import com.onetuks.libraryobject.enums.RoleType;
import com.onetuks.libraryobject.vo.ImageFile;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class MemberEntityFixture {

  private static final Random random = new Random();

  private static final long initialPoint = 30L;

  public static MemberEntity create() {
    return new MemberEntity(
        null,
        createAuthInfoEmbeddable(),
        "테스트 유저 이름",
        "테스트 유저 소개",
        Set.of(Category.MAGAZINE, Category.ART_BOOK),
        true,
        initialPoint,
        ImageFile.DEFAULT_PROFILE_IMAGE_URI,
        ImageFile.DEFAULT_PROFILE_BACKGROUND_IMAGE_URI,
        createMemberStaticEntity());
  }

  private static AuthInfoEmbeddable createAuthInfoEmbeddable() {
    return new AuthInfoEmbeddable(
        UUID.randomUUID().toString(),
        ClientProvider.values()[random.nextInt(ClientProvider.values().length)],
        Set.of(RoleType.USER));
  }

  private static MemberStaticsEntity createMemberStaticEntity() {
    return new MemberStaticsEntity(
        null,
        0L,
        0L,
        0L,
        Arrays.stream(Category.values())
            .collect(Collectors.toMap(category -> category, category -> 0L)));
  }
}
