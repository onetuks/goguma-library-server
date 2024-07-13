package com.onetuks.libraryexternal.book.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.libraryexternal.ExternalIntegrationTest;
import com.onetuks.libraryexternal.book.handler.dto.IsbnResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class IsbnSearchServiceTest extends ExternalIntegrationTest {

  @Test
  @DisplayName("중앙도서관 openAPI를 통해 ISBN으로 도서 검색을 수행한다.")
  void search() {
    // Given
    String isbn = "9791170400097";

    // When
    IsbnResult result = isbnSearchService.search(isbn);

    // Then
    assertAll(
        () -> assertThat(result.title()).isEqualTo("너와 함께라면 인생도 여행이다"),
        () -> assertThat(result.authorName()).isEqualTo("나태주"),
        () -> assertThat(result.publisher()).isEqualTo("열림원"),
        () -> assertThat(result.isbn()).isEqualTo(isbn),
        () -> assertThat(result.kdc()).isNotNull().isNotBlank(),
        () -> assertThat(result.coverImageUrl()).isNotBlank());
  }
}
