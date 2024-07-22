package com.onetuks.librarydomain.member.model;

public record Follow(Long followId, Member follower, Member followee) {}
