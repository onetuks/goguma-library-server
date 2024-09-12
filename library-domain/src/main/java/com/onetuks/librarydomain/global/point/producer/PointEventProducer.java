package com.onetuks.librarydomain.global.point.producer;

import com.onetuks.librarystream.enums.StreamKey;
import com.onetuks.librarystream.producer.event.PointEvent;
import com.onetuks.librarystream.util.MessageStreamer;
import org.springframework.stereotype.Service;

@Service
public class PointEventProducer {

  private final MessageStreamer streamer;

  public PointEventProducer(MessageStreamer streamer) {
    this.streamer = streamer;
  }

  public void creditPointForBookRegistration(long memberId) {
    streamer.addToStream(
        StreamKey.POINT_EVENT.getKey(), new PointEvent("CREDIT", "BOOK_REGISTRATION", memberId));
  }

  public void debitPointForBookRemoval(long memberId) {
    streamer.addToStream(
        StreamKey.POINT_EVENT.getKey(), new PointEvent("DEBIT", "BOOK_REGISTRATION", memberId));
  }

  public void creditPointForReviewRegistration(long memberId, boolean isWeeklyFeaturedBook) {
    if (isWeeklyFeaturedBook) {
      streamer.addToStream(
          StreamKey.POINT_EVENT.getKey(),
          new PointEvent("CREDIT", "REVIEW_REGISTRATION_EVENT", memberId));
      return;
    }

    streamer.addToStream(
        StreamKey.POINT_EVENT.getKey(),
        new PointEvent("CREDIT", "REVIEW_REGISTRATION_BASE", memberId));
  }

  public void debitPointForReviewRemoval(long memberId) {
    streamer.addToStream(
        StreamKey.POINT_EVENT.getKey(), new PointEvent("DEBIT", "REVIEW_REGISTRATION", memberId));
  }

  public void creditPointForReviewPick(long pickerMemberId, long receiverMemberId) {
    streamer.addToStream(
        StreamKey.POINT_EVENT.getKey(),
        new PointEvent("CREDIT", "REVIEW_PICK_PICKER", pickerMemberId));
    streamer.addToStream(
        StreamKey.POINT_EVENT.getKey(),
        new PointEvent("CREDIT", "REVIEW_PICK_RECEIVER", receiverMemberId));
  }

  public void debitPointForReviewPick(long pickerMemberId) {
    streamer.addToStream(
        StreamKey.POINT_EVENT.getKey(),
        new PointEvent("DEBIT", "REVIEW_PICK_PICKER", pickerMemberId));
  }

  public void creditPointForAttendance(long loginId, int attendedCount) {
    switch (attendedCount) {
      case 1 ->
          streamer.addToStream(
              StreamKey.POINT_EVENT.getKey(),
              new PointEvent("CREDIT", "ATTENDANCE_DAILY", loginId));
      case 3 ->
          streamer.addToStream(
              StreamKey.POINT_EVENT.getKey(),
              new PointEvent("CREDIT", "ATTENDANCE_3_DAYS", loginId));
      case 5 ->
          streamer.addToStream(
              StreamKey.POINT_EVENT.getKey(),
              new PointEvent("CREDIT", "ATTENDANCE_5_DAYS", loginId));
      case 10 ->
          streamer.addToStream(
              StreamKey.POINT_EVENT.getKey(),
              new PointEvent("CREDIT", "ATTENDANCE_10_DAYS", loginId));
      case 30 ->
          streamer.addToStream(
              StreamKey.POINT_EVENT.getKey(),
              new PointEvent("CREDIT", "ATTENDANCE_30_DAYS", loginId));
    }
  }

  public void removeMemberPointHistories(long memberId) {
    streamer.addToStream(
        StreamKey.POINT_EVENT.getKey(),
        new PointEvent("REMOVE", "MEMBER_POINT_HISTORIES", memberId));
  }
}
