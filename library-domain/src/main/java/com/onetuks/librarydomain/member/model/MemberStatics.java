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

  public MemberStatics increaseReviewCategoryCounts(Set<Category> categories) {
    return new MemberStatics(
        memberStaticsId,
        reviewCounts + 1,
        followerCounts,
        followingCounts,
        reviewCategoryCounts.entrySet().stream()
            .collect(
                Collectors.toMap(
                    Map.Entry::getKey,
                    entry ->
                        categories.contains(entry.getKey())
                            ? Math.min(Long.MAX_VALUE, entry.getValue() + 1)
                            : entry.getValue())));
  }

  public MemberStatics decreaseReviewCategoryCounts(Set<Category> categories) {
    return new MemberStatics(
        memberStaticsId,
        reviewCounts - 1,
        followerCounts,
        followingCounts,
        reviewCategoryCounts.entrySet().stream()
            .collect(
                Collectors.toMap(
                    Map.Entry::getKey,
                    entry ->
                        categories.contains(entry.getKey())
                            ? Math.max(0, entry.getValue() - 1)
                            : entry.getValue())));
  }

  public MemberStatics increaseFollowerCount() {
    return new MemberStatics(
        memberStaticsId, reviewCounts, followerCounts + 1, followingCounts, reviewCategoryCounts);
  }

  public MemberStatics decreaseFollowerCount() {
    return new MemberStatics(
        memberStaticsId, reviewCounts, followerCounts - 1, followingCounts, reviewCategoryCounts);
  }

  public MemberStatics increaseFolloweeCount() {
    return new MemberStatics(
        memberStaticsId, reviewCounts, followerCounts, followingCounts + 1, reviewCategoryCounts);
  }

  public MemberStatics decreaseFolloweeCount() {
    return new MemberStatics(
        memberStaticsId, reviewCounts, followerCounts, followingCounts - 1, reviewCategoryCounts);
  }
}
