package com.onetuks.librarydomain.member.model;

import java.util.List;

public record MemberStatics(
    Long memberStaticsId,
    long reviewCounts,
    long followerCounts,
    long followingCounts,
    List<Long> reviewCategoryCounts) {}
