package com.onetuks.librarydomain.review.service;

import com.onetuks.librarydomain.global.point.service.PointService;
import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.librarydomain.member.repository.MemberRepository;
import com.onetuks.librarydomain.review.model.Review;
import com.onetuks.librarydomain.review.model.ReviewPick;
import com.onetuks.librarydomain.review.repository.ReviewPickRepository;
import com.onetuks.librarydomain.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewPickService {

  private final ReviewPickRepository reviewPickRepository;
  private final MemberRepository memberRepository;
  private final ReviewRepository reviewRepository;

  private final PointService pointService;

  public ReviewPickService(
      ReviewPickRepository reviewPickRepository,
      MemberRepository memberRepository,
      ReviewRepository reviewRepository,
      PointService pointService) {
    this.reviewPickRepository = reviewPickRepository;
    this.memberRepository = memberRepository;
    this.reviewRepository = reviewRepository;
    this.pointService = pointService;
  }

  @Transactional
  public ReviewPick register(long loginId, long reviewId) {
    Member picker = memberRepository.read(loginId);
    Review review = reviewRepository.read(reviewId);

    pointService.creditPointForReviewPick(picker.memberId(), review.member().memberId());

    return reviewPickRepository.create(new ReviewPick(null, picker, review));
  }
}
