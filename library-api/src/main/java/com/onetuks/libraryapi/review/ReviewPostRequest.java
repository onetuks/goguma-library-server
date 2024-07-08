package com.onetuks.libraryapi.review;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record ReviewPostRequest(
    @NotBlank @Length(min = 2, max = 25) String reviewTitle,
    @NotBlank @Length(min = 20, max = 1_000) String reviewContent) {}
