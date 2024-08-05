package com.onetuks.librarydomain.review.model;

import com.onetuks.librarydomain.book.model.Book;
import com.onetuks.librarydomain.member.model.Member;
import java.time.LocalDateTime;

public record Review(
    Long reviewId,
    Member member,
    Book book,
    String reviewTitle,
    String reviewContent,
    long pickCount,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {

  public Review(Member member, Book book, String reviewTitle, String reviewContent) {
    this(null, member, book, reviewTitle, reviewContent, 0, null, null);
  }

  public Review increasePickCount() {
    return new Review(
        reviewId,
        member,
        book,
        reviewTitle,
        reviewContent,
        Math.min(Long.MAX_VALUE, pickCount + 1),
        createdAt,
        updatedAt);
  }

  public Review decreasePickCount() {
    return new Review(
        reviewId,
        member,
        book,
        reviewTitle,
        reviewContent,
        Math.max(0, pickCount - 1),
        createdAt,
        updatedAt);
  }
}
