package com.onetuks.librarydomain.review.service;

import com.onetuks.librarydomain.member.repository.MemberRepository;
import com.onetuks.librarydomain.review.repository.ReviewPickRepository;
import com.onetuks.librarydomain.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
public class ReviewPickService {

  private final ReviewPickRepository reviewPickRepository;
  private final MemberRepository memberRepository;
  private final ReviewRepository reviewRepository;

  public ReviewPickService(
      ReviewPickRepository reviewPickRepository,
      MemberRepository memberRepository,
      ReviewRepository reviewRepository) {
    this.reviewPickRepository = reviewPickRepository;
    this.memberRepository = memberRepository;
    this.reviewRepository = reviewRepository;
  }
}
