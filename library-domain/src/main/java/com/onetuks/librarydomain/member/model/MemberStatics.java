package com.onetuks.librarydomain.member.model;

import com.onetuks.libraryobject.enums.Category;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public record MemberStatics(
    Long memberStaticsId,
    long reviewCounts,
    long followerCounts,
    long followingCounts,
    Map<Category, Long> reviewCategoryCounts) {

  public static MemberStatics init() {
    return new MemberStatics(
        null,
        0L,
        0L,
        0L,
        Arrays.stream(Category.values())
            .map(category -> Map.entry(category, 0L))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
  }

  public MemberStatics update(Set<Category> categories) {
    return new MemberStatics(
        memberStaticsId,
        reviewCounts,
        followerCounts,
        followingCounts,
        reviewCategoryCounts.entrySet().stream()
            .collect(
                Collectors.toMap(
                    Map.Entry::getKey,
                    entry ->
                        categories.contains(entry.getKey())
                            ? entry.getValue() + 1
                            : entry.getValue())));
  }
}
