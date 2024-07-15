package com.onetuks.libraryapi.review.dto.response;

import com.onetuks.librarydomain.review.model.Review;
import com.onetuks.librarydomain.review.model.ReviewPick;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;

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

  public record ReviewResponses(Page<ReviewResponse> responses) {

    public static ReviewResponses from(Page<Review> results) {
      return new ReviewResponses(results.map(ReviewResponse::from));
    }

    public static ReviewResponses fromPicks(Page<ReviewPick> results) {
      return new ReviewResponses(results.map(ReviewPick::review).map(ReviewResponse::from));
    }
  }
}
