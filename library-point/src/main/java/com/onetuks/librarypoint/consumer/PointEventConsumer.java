package com.onetuks.librarypoint.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetuks.librarypoint.service.PointService;
import com.onetuks.librarypoint.service.model.vo.Activity;
import com.onetuks.librarypoint.service.model.vo.CreditType;
import com.onetuks.librarystream.consumer.StreamConsumer;
import com.onetuks.librarystream.enums.ConsumerGroup;
import com.onetuks.librarystream.enums.StreamKey;
import com.onetuks.librarystream.producer.event.PointEvent;
import com.onetuks.librarystream.util.MessageStreamer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.stereotype.Service;

@Service
public class PointEventConsumer extends StreamConsumer {

  private static final Logger log = LoggerFactory.getLogger(PointEventConsumer.class);
  private static final ObjectMapper objectMapper = new ObjectMapper();

  private final MessageStreamer streamer;
  private final PointService pointService;

  public PointEventConsumer(MessageStreamer streamer, PointService pointService) {
    this.streamer = streamer;
    this.pointService = pointService;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    streamer.createStreamConsumerGroup(
        StreamKey.POINT_EVENT.getKey(), ConsumerGroup.POINT_EVENT_GROUP.getGroup());
    this.setUpAndStartConsumer(
        StreamKey.POINT_EVENT.getKey(),
        ConsumerGroup.POINT_EVENT_GROUP.getGroup(),
        ConsumerGroup.POINT_EVENT_GROUP.getGroup() + "-consumer",
        streamer.createStreamMessageListenerContainer());
  }

  @Override
  public void onMessage(ObjectRecord<String, String> message) {
    try {
      PointEvent pointEvent = objectMapper.readValue(message.getValue(), PointEvent.class);
      forwardByCreditType(pointEvent);
      streamer.ackStream(this.getStreamKey(), this.getConsumerGroupName(), message);
      streamer.deleteFromStream(this.getStreamKey(), message.getId());
    } catch (NullPointerException e) {
      log.warn("Invalid Point Event Message", e);
    } catch (Exception e) {
      log.warn("Failed to process point event", e);
    }
  }

  public void forwardByCreditType(PointEvent pointEvent) {
    CreditType creditType = CreditType.valueOf(pointEvent.getCreditType());
    Activity activity = Activity.valueOf(pointEvent.getActivity());
    long memberId = Long.parseLong(pointEvent.getMemberId());

    if (creditType == CreditType.CREDIT) {
      forwardToCreditService(activity, memberId);
      log.info("Credit Point Event Success - memberId: {}, activity: {}", memberId, activity);
    } else if (creditType == CreditType.DEBIT) {
      forwardToDebitService(activity, memberId);
      log.info("Debit Point Event Success - memberId: {}, activity: {}", memberId, activity);
    } else {
      pointService.removeMemberPointHistories(memberId);
      log.info("Remove Member Point Histories Success - memberId: {}", memberId);
    }
  }

  private void forwardToCreditService(Activity activity, long memberId) {
    switch (activity) {
      case BOOK_REGISTRATION -> pointService.creditPointForBookRegistration(memberId);
      case REVIEW_REGISTRATION_EVENT ->
          pointService.creditPointForReviewRegistrationEvent(memberId);
      case REVIEW_REGISTRATION_BASE -> pointService.creditPointForReviewRegistration(memberId);
      case REVIEW_PICK_PICKER -> pointService.creditPointForReviewPicker(memberId);
      case REVIEW_PICK_RECEIVER -> pointService.creditPointForReviewReceiver(memberId);
      case MEMBER_POINT_HISTORIES -> {}
      default -> pointService.creditPointForAttendance(memberId, activity);
    }
  }

  private void forwardToDebitService(Activity activity, long memberId) {
    switch (activity) {
      case BOOK_REGISTRATION -> pointService.debitPointForBookRemoval(memberId);
      case REVIEW_REGISTRATION_BASE -> pointService.debitPointForReviewRemoval(memberId);
      case REVIEW_PICK_PICKER -> pointService.debitPointForReviewPicker(memberId);
    }
  }
}
