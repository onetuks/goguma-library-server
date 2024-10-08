package com.onetuks.librarydomain.review.service;

import com.onetuks.librarydomain.global.point.producer.PointEventProducer;
import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.librarydomain.member.repository.MemberRepository;
import com.onetuks.librarydomain.review.model.Review;
import com.onetuks.librarydomain.review.model.ReviewPick;
import com.onetuks.librarydomain.review.repository.ReviewPickRepository;
import com.onetuks.librarydomain.review.repository.ReviewRepository;
import com.onetuks.libraryobject.enums.CacheName;
import com.onetuks.libraryobject.exception.ApiAccessDeniedException;
import java.util.Objects;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewPickService {

  private final ReviewPickRepository reviewPickRepository;
  private final MemberRepository memberRepository;
  private final ReviewRepository reviewRepository;

  private final PointEventProducer pointEventProducer;

  public ReviewPickService(
      ReviewPickRepository reviewPickRepository,
      MemberRepository memberRepository,
      ReviewRepository reviewRepository,
      PointEventProducer pointEventProducer) {
    this.reviewPickRepository = reviewPickRepository;
    this.memberRepository = memberRepository;
    this.reviewRepository = reviewRepository;
    this.pointEventProducer = pointEventProducer;
  }

  @CachePut(value = CacheName.REVIEW_PICKS, key = "#loginId" + "-" + "#reviewId")
  @Transactional
  public ReviewPick register(long loginId, long reviewId) {
    Member picker = memberRepository.read(loginId);
    Review review = reviewRepository.readWithLock(reviewId);

    pointEventProducer.creditPointForReviewPick(picker.memberId(), review.member().memberId());
    reviewRepository.update(review.increasePickCount());

    return reviewPickRepository.create(new ReviewPick(null, picker, review));
  }

  @CacheEvict(value = CacheName.REVIEW_PICKS, allEntries = true)
  @Transactional
  public void remove(long loginId, long reviewPickId) {
    Member picker = memberRepository.read(loginId);
    ReviewPick reviewPick = reviewPickRepository.read(reviewPickId);

    if (!Objects.equals(reviewPick.member().memberId(), picker.memberId())) {
      throw new ApiAccessDeniedException("해당 서평픽에 대한 권한이 없는 멤버입니다.");
    }

    pointEventProducer.debitPointForReviewPick(picker.memberId());
    reviewRepository.update(reviewPick.review().decreasePickCount());

    reviewPickRepository.delete(reviewPick.reviewPickId());
  }

  @Transactional(readOnly = true)
  public Page<ReviewPick> searchAll(long loginId, Pageable pageable) {
    return reviewPickRepository.readAll(loginId, pageable);
  }

  @Cacheable(value = CacheName.REVIEW_PICKS, key = "#loginId" + "-" + "#reviewId")
  @Transactional(readOnly = true)
  public ReviewPick searchExistence(long loginId, long reviewId) {
    return reviewPickRepository.read(loginId, reviewId);
  }

  @Transactional(readOnly = true)
  public Long searchCount(long loginId, long reviewId) {
    return reviewPickRepository.readCount(loginId, reviewId);
  }
}
