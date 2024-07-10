package com.onetuks.libraryapi.review.dto.response;

import com.onetuks.librarydomain.review.model.Review;
import java.time.LocalDateTime;

public record ReviewResponse(
    long reviewId,
    long memberId,
    long bookId,
    String reviewTitle,
    String reviewContent,
    long pickCount,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {

  public static ReviewResponse from(Review review) {
    return new ReviewResponse(
        review.reviewId(),
        review.member().memberId(),
        review.book().bookId(),
        review.reviewTitle(),
        review.reviewContent(),
        review.pickCount(),
        review.createdAt(),
        review.updatedAt());
  }
}
