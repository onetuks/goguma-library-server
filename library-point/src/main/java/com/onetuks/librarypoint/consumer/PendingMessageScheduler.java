package com.onetuks.librarypoint.consumer;

import static com.onetuks.librarystream.util.MessageStreamer.ERROR_COUNT_KEY;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetuks.librarystream.producer.event.PointEvent;
import com.onetuks.librarystream.util.MessageStreamer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.PendingMessage;
import org.springframework.data.redis.connection.stream.PendingMessages;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@EnableScheduling
@Component
public class PendingMessageScheduler implements InitializingBean {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  private String streamKey;
  private String consumerGroupName;
  private String consumerName;

  private final MessageStreamer streamer;
  private final PointEventConsumer consumer;

  public PendingMessageScheduler(MessageStreamer streamer, PointEventConsumer consumer) {
    this.streamer = streamer;
    this.consumer = consumer;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    this.streamKey = consumer.getStreamKey();
    this.consumerGroupName = consumer.getConsumerGroupName();
    this.consumerName = consumer.getConsumerName();
  }

  @Scheduled(fixedRate = 10_000)
  public void processPendingMessage() {
    PendingMessages pendingMessages =
        streamer.findStreamPendingMessages(streamKey, consumerGroupName, consumerName);

    for (PendingMessage pendingMessage : pendingMessages) {
      try {
        ObjectRecord<String, String> messageToProcess =
            streamer.findStreamMessageById(this.streamKey, pendingMessage.getIdAsString());

        if (messageToProcess == null) {
          continue;
        }

        streamer.claimStream(pendingMessage, consumerName);

        int errorCount =
            (int) streamer.getRedisValue(ERROR_COUNT_KEY, pendingMessage.getIdAsString());
        if (errorCount >= 5) {
          log.warn("재처리 시도 제한 초과");
        } else if (pendingMessage.getTotalDeliveryCount() >= 2) {
          log.warn("delivery 제한 횟수 초과");
        } else {
          PointEvent pointEvent =
              objectMapper.readValue(messageToProcess.getValue(), PointEvent.class);
          consumer.forwardByCreditType(pointEvent);
        }

        streamer.ackStream(streamKey, consumerGroupName, messageToProcess);
        streamer.deleteFromStream(streamKey, pendingMessage.getId());
      } catch (Exception e) {
        streamer.increaseRedisValue(ERROR_COUNT_KEY, pendingMessage.getIdAsString());
        log.warn(
            "Failed to process pending message: gr: {} / cs: {}",
            pendingMessage.getGroupName(),
            pendingMessage.getConsumerName());
      }
    }
  }
}
