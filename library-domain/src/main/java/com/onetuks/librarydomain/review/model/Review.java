package com.onetuks.librarydomain.review.model;

import com.onetuks.librarydomain.book.model.Book;
import com.onetuks.librarydomain.member.model.Member;

public record Review(
    Long reviewId,
    Member member,
    Book book,
    String reviewTitle,
    String reviewContent,
    long pickCount) {}
