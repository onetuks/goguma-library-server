package com.onetuks.librarydomain.review.service;

import com.onetuks.librarydomain.book.repository.BookRepository;
import com.onetuks.librarydomain.member.repository.MemberRepository;
import com.onetuks.librarydomain.review.model.Review;
import com.onetuks.librarydomain.review.repository.ReviewRepository;
import com.onetuks.librarydomain.review.service.dto.param.ReviewParam;
import com.onetuks.libraryobject.exception.ApiAccessDeniedException;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewService {

  private final ReviewRepository reviewRepository;
  private final MemberRepository memberRepository;
  private final BookRepository bookRepository;

  public ReviewService(
      ReviewRepository reviewRepository,
      MemberRepository memberRepository,
      BookRepository bookRepository) {
    this.reviewRepository = reviewRepository;
    this.memberRepository = memberRepository;
    this.bookRepository = bookRepository;
  }

  @Transactional
  public Review register(long loginId, long bookId, ReviewParam param) {
    // todo 서평 등록 시 포인트 지급
    return reviewRepository.create(
        new Review(
            memberRepository.read(loginId),
            bookRepository.read(bookId),
            param.reviewTitle(),
            param.reviewContent()));
  }

  @Transactional
  public Review edit(long loginId, long reviewId, ReviewParam param) {
    Review review = reviewRepository.read(reviewId);

    if (review.member().memberId() != loginId) {
      throw new ApiAccessDeniedException("해당 서평에 대한 권한이 없는 멤버입니다.");
    }

    return reviewRepository.update(
        new Review(
            review.reviewId(),
            review.member(),
            review.book(),
            param.reviewTitle(),
            param.reviewContent(),
            review.pickCount(),
            review.createdAt(),
            LocalDateTime.now()));
  }
}
