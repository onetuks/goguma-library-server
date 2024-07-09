package com.onetuks.libraryapi.review;

import com.onetuks.librarydomain.review.service.ReviewService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/reviews")
public class ReviewRestController {

  private final ReviewService reviewService;

  public ReviewRestController(ReviewService reviewService) {
    this.reviewService = reviewService;
  }
}
