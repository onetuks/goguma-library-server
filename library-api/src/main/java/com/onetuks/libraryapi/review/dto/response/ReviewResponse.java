package com.onetuks.libraryapi.review.dto.response;

import com.onetuks.librarydomain.review.model.Review;
import com.onetuks.librarydomain.review.model.ReviewPick;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

public record ReviewResponse(
    long reviewId,
    long memberId,
    String nickname,
    long bookId,
    String bookTitle,
    String reviewTitle,
    String reviewContent,
    long pickCount,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {

  public static ReviewResponse from(Review review) {
    return new ReviewResponse(
        review.reviewId(),
        review.member().memberId(),
        review.member().nickname().value(),
        review.book().bookId(),
        review.book().title(),
        review.reviewTitle(),
        review.reviewContent(),
        review.pickCount(),
        review.createdAt(),
        review.updatedAt());
  }

  public record ReviewPageResponses(Page<ReviewResponse> responses) {

    public static ReviewPageResponses from(Page<Review> results) {
      return new ReviewPageResponses(results.map(ReviewResponse::from));
    }

    public static ReviewPageResponses fromPicks(Page<ReviewPick> results) {
      return new ReviewPageResponses(results.map(ReviewPick::review).map(ReviewResponse::from));
    }
  }

  public record ReviewSliceResponses(Slice<ReviewResponse> responses) {

    public static ReviewSliceResponses from(Slice<Review> results) {
      return new ReviewSliceResponses(results.map(ReviewResponse::from));
    }
  }
}
