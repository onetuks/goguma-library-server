package com.onetuks.librarydomain.global.point.service;

import com.onetuks.librarydomain.global.point.service.dto.PointEvent;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class PointEventProducer {

  private final RedisTemplate<String, Object> redisTemplate;

  public PointEventProducer(RedisTemplate<String, Object> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  public void creditPointForBookRegistration(long memberId) {
    PointEvent event = new PointEvent("CREDIT", "BOOK_REGISTRATION", memberId);

    redisTemplate.opsForStream().add(StreamRecords.newRecord().ofObject(event).withStreamKey(memberId));
  }

  public void debitPointForBookRemoval(long memberId);

  public void creditPointForReviewRegistration(long memberId, boolean isWeeklyFeaturedBook);

  public void debitPointForReviewRemoval(long memberId);

  public void creditPointForReviewPick(long pickerMemberId, long receiverMemberId);

  public void debitPointForReviewPick(long pickerMemberId);

  public void creditPointForAttendance(long loginId, int attendedCount);
}
