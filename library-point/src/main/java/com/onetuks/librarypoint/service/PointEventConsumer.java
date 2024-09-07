package com.onetuks.librarypoint.service;

import com.onetuks.librarydomain.global.point.service.PointService;
import java.util.Objects;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Service;

@Service
public class PointEventConsumer implements StreamListener<String, MapRecord<String, String, String>> {

  private final PointService pointService;

  public PointEventConsumer(PointService pointService) {
    this.pointService = pointService;
  }

  @Override
  public void onMessage(MapRecord<String, String, String> record) {
    String memberId = record.getValue().get("memberId");
    int points = Integer.parseInt(record.getValue().get("points"));
    String actionType = record.getValue().get("actionType");

    if (Objects.equals("INCREASE", actionType)) {
      pointService.creditPointForReview
    }
  }
}
