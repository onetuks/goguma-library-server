package com.onetuks.libraryapi.review.dto.request;

import com.onetuks.librarydomain.review.service.dto.param.ReviewParam;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record ReviewRequest(
    @NotBlank @Length(min = 2, max = 25) String reviewTitle,
    @NotBlank @Length(min = 20, max = 1_000) String reviewContent) {

  public ReviewParam to() {
    return new ReviewParam(reviewTitle(), reviewContent());
  }
}
