package com.onetuks.librarypoint.consumer;

import static org.assertj.core.api.Assertions.assertThat;

import com.onetuks.dbstorage.member.entity.MemberEntity;
import com.onetuks.libraryobject.exception.NoSuchEntityException;
import com.onetuks.librarypoint.CorePointIntegrationTest;
import com.onetuks.librarypoint.fixture.MemberEntityFixture;
import com.onetuks.librarypoint.service.model.vo.Activity;
import com.onetuks.librarystream.producer.event.PointEvent;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class PointEventConsumerTest extends CorePointIntegrationTest {

  private static final Logger log = LoggerFactory.getLogger(PointEventConsumerTest.class);

  @Test
  @DisplayName("동시에 여러 명이 한 서평에 픽해도 모든 포인트가 리시버에게 지급된다.")
  void creditPointForReceiver_ConcurrencyTest() throws InterruptedException {
    // Given
    int count = 10;
    MemberEntity receiver = memberEntityJpaRepository.save(MemberEntityFixture.create());
    ExecutorService executorService = Executors.newFixedThreadPool(count);
    CountDownLatch latch = new CountDownLatch(1);

    memberEntityJpaRepository.flush();

    // When
    Runnable task = () -> {
      try {
        latch.await();
        messageStreamer.addToStream(
            "POINT_EVENT", new PointEvent("CREDIT", "REVIEW_PICK_RECEIVER", receiver.getMemberId()));
      } catch (Exception e) {
        log.warn("Error occurred while executing task", e);
      }
    };

    for (int i = 0; i < count; i++) {
      executorService.submit(task);
    }

    latch.countDown();
    executorService.shutdown();
    executorService.awaitTermination(10, TimeUnit.SECONDS);

    memberEntityJpaRepository.flush();

    // Then
    MemberEntity result = memberEntityJpaRepository.findById(receiver.getMemberId())
        .orElseThrow(() -> new NoSuchEntityException("존재하지 않는 멤버입니다."));

    assertThat(result.getPoints())
        .isEqualTo(receiver.getPoints() + count * Activity.REVIEW_PICK_RECEIVER.getPoints());
  }
}