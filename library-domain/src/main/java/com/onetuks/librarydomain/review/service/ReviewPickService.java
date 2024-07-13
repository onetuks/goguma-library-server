package com.onetuks.librarydomain.review.service;

import static com.onetuks.librarydomain.member.repository.PointRepository.REVIEW_PICK_GIVER_POINT;
import static com.onetuks.librarydomain.member.repository.PointRepository.REVIEW_PICK_RECEIVER_POINT;

import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.librarydomain.member.repository.MemberRepository;
import com.onetuks.librarydomain.member.repository.PointRepository;
import com.onetuks.librarydomain.review.model.Review;
import com.onetuks.librarydomain.review.model.ReviewPick;
import com.onetuks.librarydomain.review.repository.DailyPointLimitRepository;
import com.onetuks.librarydomain.review.repository.ReviewPickRepository;
import com.onetuks.librarydomain.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewPickService {

  private final ReviewPickRepository reviewPickRepository;
  private final MemberRepository memberRepository;
  private final ReviewRepository reviewRepository;
  private final PointRepository pointRepository;
  private final DailyPointLimitRepository dailyPointLimitRepository;

  public ReviewPickService(
      ReviewPickRepository reviewPickRepository,
      MemberRepository memberRepository,
      ReviewRepository reviewRepository,
      PointRepository pointRepository,
      DailyPointLimitRepository dailyPointLimitRepository) {
    this.reviewPickRepository = reviewPickRepository;
    this.memberRepository = memberRepository;
    this.reviewRepository = reviewRepository;
    this.pointRepository = pointRepository;
    this.dailyPointLimitRepository = dailyPointLimitRepository;
  }

  @Transactional
  public ReviewPick register(long loginId, long reviewId) {
    Member member = memberRepository.read(loginId);
    Review review = reviewRepository.read(reviewId);

    if (dailyPointLimitRepository.isCreditable(loginId)) {
      dailyPointLimitRepository.save(loginId, dailyPointLimitRepository.find(loginId) + 1);
      pointRepository.creditPoints(member.memberId(), REVIEW_PICK_GIVER_POINT);
      pointRepository.creditPoints(review.member().memberId(), REVIEW_PICK_RECEIVER_POINT);
    }

    return reviewPickRepository.create(new ReviewPick(null, member, review));
  }
}
