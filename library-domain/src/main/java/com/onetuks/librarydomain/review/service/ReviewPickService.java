package com.onetuks.librarydomain.review.service;

import com.onetuks.librarydomain.global.point.service.PointService;
import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.librarydomain.member.repository.MemberRepository;
import com.onetuks.librarydomain.review.model.Review;
import com.onetuks.librarydomain.review.model.ReviewPick;
import com.onetuks.librarydomain.review.repository.ReviewPickRepository;
import com.onetuks.librarydomain.review.repository.ReviewRepository;
import com.onetuks.libraryobject.exception.ApiAccessDeniedException;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

  @Transactional
  public void remove(long loginId, long reviewPickId) {
    Member picker = memberRepository.read(loginId);
    ReviewPick reviewPick = reviewPickRepository.read(reviewPickId);

    if (!Objects.equals(reviewPick.member().memberId(), picker.memberId())) {
      throw new ApiAccessDeniedException("해당 서평픽에 대한 권한이 없는 멤버입니다.");
    }

    pointService.debitPointForReviewPick(picker.memberId());

    reviewPickRepository.delete(reviewPick.reviewPickId());
  }

  @Transactional(readOnly = true)
  public Page<ReviewPick> searchAll(long loginId, Pageable pageable) {
    return reviewPickRepository.readAll(loginId, pageable);
  }

  @Transactional(readOnly = true)
  public boolean searchExistence(long loginId, long reviewId) {
    return reviewPickRepository.read(loginId, reviewId);
  }
}
