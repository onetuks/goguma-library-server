package com.onetuks.librarydomain.review.service;

import com.onetuks.librarydomain.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

  private final ReviewRepository reviewRepository;

  public ReviewService(ReviewRepository reviewRepository) {
    this.reviewRepository = reviewRepository;
  }
}
