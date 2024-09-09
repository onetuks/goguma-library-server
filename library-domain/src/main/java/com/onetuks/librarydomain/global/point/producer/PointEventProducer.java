package com.onetuks.librarydomain.global.point.producer;

import com.onetuks.librarydomain.global.point.producer.dto.PointEvent;
import com.onetuks.librarydomain.global.util.MessageStreamer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class PointEventProducer {

  private final MessageStreamer streamer;

  public PointEventProducer(MessageStreamer streamer) {
    this.streamer = streamer;
  }

  public void creditPointForBookRegistration(long memberId) {
    streamer.sendMessage(memberId, new PointEvent("CREDIT", "BOOK_REGISTRATION", memberId));
  }

  public void debitPointForBookRemoval(long memberId) {
    streamer.sendMessage(memberId, new PointEvent("DEBIT", "BOOK_REGISTRATION", memberId));
  }

  public void creditPointForReviewRegistration(long memberId, boolean isWeeklyFeaturedBook) {
    if (isWeeklyFeaturedBook) {
      streamer.sendMessage(
          memberId, new PointEvent("CREDIT", "REVIEW_REGISTRATION_EVENT", memberId));
      return;
    }

    streamer.sendMessage(memberId, new PointEvent("CREDIT", "REVIEW_REGISTRATION_BASE", memberId));
  }

  public void debitPointForReviewRemoval(long memberId) {
    streamer.sendMessage(memberId, new PointEvent("DEBIT", "REVIEW_REGISTRATION", memberId));
  }

  public void creditPointForReviewPick(long pickerMemberId, long receiverMemberId) {
    streamer.sendMessage(
        pickerMemberId, new PointEvent("CREDIT", "REVIEW_PICK_PICKER", pickerMemberId));
    streamer.sendMessage(
        receiverMemberId, new PointEvent("CREDIT", "REVIEW_PICK_RECEIVER", receiverMemberId));
  }

  public void debitPointForReviewPick(long pickerMemberId) {
    streamer.sendMessage(
        pickerMemberId, new PointEvent("DEBIT", "REVIEW_PICK_PICKER", pickerMemberId));
  }

  public void creditPointForAttendance(long loginId, int attendedCount) {
    streamer.sendMessage(loginId, new PointEvent("CREDIT", "ATTENDANCE_DAILY", loginId));

    switch (attendedCount) {
      case 3 ->
          streamer.sendMessage(loginId, new PointEvent("CREDIT", "ATTENDANCE_3_DAYS", loginId));
      case 5 ->
          streamer.sendMessage(loginId, new PointEvent("CREDIT", "ATTENDANCE_5_DAYS", loginId));
      case 10 ->
          streamer.sendMessage(loginId, new PointEvent("CREDIT", "ATTENDANCE_10_DAYS", loginId));
      case 30 ->
          streamer.sendMessage(loginId, new PointEvent("CREDIT", "ATTENDANCE_30_DAYS", loginId));
    }
  }
}
