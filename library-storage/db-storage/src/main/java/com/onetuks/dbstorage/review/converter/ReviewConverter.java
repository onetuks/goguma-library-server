package com.onetuks.dbstorage.review.converter;

import com.onetuks.dbstorage.book.converter.BookConverter;
import com.onetuks.dbstorage.member.converter.MemberConverter;
import com.onetuks.dbstorage.review.entity.ReviewEntity;
import com.onetuks.librarydomain.review.model.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewConverter {

  private final MemberConverter memberConverter;
  private final BookConverter bookConverter;

  public ReviewConverter(MemberConverter memberConverter, BookConverter bookConverter) {
    this.memberConverter = memberConverter;
    this.bookConverter = bookConverter;
  }

  public ReviewEntity toEntity(Review review) {
    return new ReviewEntity(
        review.reviewId(),
        memberConverter.toEntity(review.member()),
        bookConverter.toEntity(review.book()),
        review.reviewTitle(),
        review.reviewContent(),
        review.pickCount());
  }

  public Review toDomain(ReviewEntity reviewEntity) {
    return new Review(
        reviewEntity.getReviewId(),
        memberConverter.toDomain(reviewEntity.getMemberEntity()),
        bookConverter.toDomain(reviewEntity.getBookEntity()),
        reviewEntity.getReviewTitle(),
        reviewEntity.getReviewContent(),
        reviewEntity.getPickCount(),
        reviewEntity.getCreatedAt(),
        reviewEntity.getUpdatedAt());
  }
}
