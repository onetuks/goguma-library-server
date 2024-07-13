package com.onetuks.libraryapi.review.controller;

import com.onetuks.librarydomain.review.service.ReviewPickService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/reviews/picks")
public class ReviewPickRestController {

  private final ReviewPickService reviewPickService;

  public ReviewPickRestController(ReviewPickService reviewPickService) {
    this.reviewPickService = reviewPickService;
  }
}
