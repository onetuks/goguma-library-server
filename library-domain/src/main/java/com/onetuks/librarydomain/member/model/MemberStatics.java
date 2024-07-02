package com.onetuks.librarydomain.member.model;

import java.util.List;
import java.util.stream.IntStream;

public record MemberStatics(
    Long memberStaticsId,
    long reviewCounts,
    long followerCounts,
    long followingCounts,
    List<Long> reviewCategoryCounts) {

  public static MemberStatics init() {
    return new MemberStatics(
        null, 0L, 0L, 0L, IntStream.range(0, 10).mapToLong(i -> 0L).boxed().toList());
  }
}
