package com.onetuks.dbstorage.review.converter;

import com.onetuks.dbstorage.member.converter.MemberConverter;
import com.onetuks.dbstorage.review.entity.ReviewPickEntity;
import com.onetuks.librarydomain.review.model.ReviewPick;
import org.springframework.stereotype.Component;

@Component
public class ReviewPickConverter {

  private final MemberConverter memberConverter;
  private final ReviewConverter reviewConverter;

  public ReviewPickConverter(MemberConverter memberConverter, ReviewConverter reviewConverter) {
    this.memberConverter = memberConverter;
    this.reviewConverter = reviewConverter;
  }

  public ReviewPickEntity toEntity(ReviewPick model) {
    return new ReviewPickEntity(
        model.reviewPickId(),
        memberConverter.toEntity(model.member()),
        reviewConverter.toEntity(model.review()));
  }

  public ReviewPick toModel(ReviewPickEntity entity) {
    return new ReviewPick(
        entity.getReviewPickId(),
        memberConverter.toModel(entity.getMemberEntity()),
        reviewConverter.toModel(entity.getReviewEntity()));
  }
}
