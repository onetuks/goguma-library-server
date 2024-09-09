package com.onetuks.librarypoint.consumer;

import com.onetuks.librarypoint.service.PointService;
import com.onetuks.librarypoint.service.model.vo.Activity;
import com.onetuks.librarypoint.service.model.vo.CreditType;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Service;

@Service
public class PointEventConsumer
    implements StreamListener<String, MapRecord<String, String, String>> {

  private static final Logger log = LoggerFactory.getLogger(PointEventConsumer.class);

  private final PointService pointService;

  public PointEventConsumer(PointService pointService) {
    this.pointService = pointService;
  }

  @Override
  public void onMessage(MapRecord<String, String, String> record) {
    try {
      CreditType creditType =
          CreditType.valueOf(Objects.requireNonNull(record.getValue().get("creditType")));
      Activity activity =
          Activity.valueOf(Objects.requireNonNull(record.getValue().get("activity")));
      long memberId = Long.parseLong(Objects.requireNonNull(record.getValue().get("memberId")));

      if (creditType == CreditType.CREDIT) {
        forwardToCreditService(activity, memberId);
        log.info("Credit Point Event Success - memberId: {}, activity: {}", memberId, activity);
        return;
      }

      forwardToDebitService(activity, memberId);
      log.info("Debit Point Event Success - memberId: {}, activity: {}", memberId, activity);

    } catch (NullPointerException e) {
      log.warn("Invalid Point Event Message", e);
    } catch (Exception e) {
      log.warn("Failed to process point event", e);
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
