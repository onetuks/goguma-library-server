package com.onetuks.librarypoint.repository.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.onetuks.librarypoint.repository.CorePointIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;

class PointRepositoryImplTest extends CorePointIntegrationTest {

  @Test
  @DisplayName("포인트 변동값이 음수인 경우 예외를 던진다.")
  void creditPoint_NegativePoint_ExceptionThrown() {
    // Given
    long memberId = 101L;
    long negativePoint = -1L;

    // When & Then
    assertThatThrownBy(() -> pointRepository.creditPoints(memberId, negativePoint))
        .isInstanceOf(InvalidDataAccessApiUsageException.class);
  }
}
