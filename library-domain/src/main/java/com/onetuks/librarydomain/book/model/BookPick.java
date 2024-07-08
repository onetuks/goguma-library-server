package com.onetuks.librarydomain.book.model;

import com.onetuks.librarydomain.member.model.Member;

public record BookPick(Long bookPickId, Member member, Book book) {}
