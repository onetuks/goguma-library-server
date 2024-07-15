package com.onetuks.librarydomain.review.model;

import com.onetuks.librarydomain.member.model.Member;

public record ReviewPick(Long reviewPickId, Member member, Review review) {}
