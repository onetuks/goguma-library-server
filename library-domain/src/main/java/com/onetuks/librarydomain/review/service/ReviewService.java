package com.onetuks.librarydomain.review.service;

import com.onetuks.librarydomain.book.repository.BookRepository;
import com.onetuks.librarydomain.member.repository.MemberRepository;
import com.onetuks.librarydomain.review.model.Review;
import com.onetuks.librarydomain.review.repository.ReviewRepository;
import com.onetuks.librarydomain.review.service.dto.param.ReviewPostParam;
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
  public Review register(long loginId, long bookId, ReviewPostParam param) {
    // todo 서평 등록 시 포인트 지급
    return reviewRepository.create(
        new Review(
            memberRepository.read(loginId),
            bookRepository.read(bookId),
            param.reviewTitle(),
            param.reviewContent()));
  }
}
