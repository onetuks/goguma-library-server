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

  public ReviewEntity toEntity(Review model) {
    return new ReviewEntity(
        model.reviewId(),
        memberConverter.toEntity(model.member()),
        bookConverter.toEntity(model.book()),
        model.reviewTitle(),
        model.reviewContent(),
        model.pickCount());
  }

  public Review toModel(ReviewEntity entity) {
    return new Review(
        entity.getReviewId(),
        memberConverter.toModel(entity.getMemberEntity()),
        bookConverter.toModel(entity.getBookEntity()),
        entity.getReviewTitle(),
        entity.getReviewContent(),
        entity.getPickCount(),
        entity.getCreatedAt(),
        entity.getUpdatedAt());
  }
}
