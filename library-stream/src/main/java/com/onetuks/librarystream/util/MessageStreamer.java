package com.onetuks.librarystream.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetuks.librarystream.producer.event.MessageEvent;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.output.StatusOutput;
import io.lettuce.core.protocol.CommandArgs;
import io.lettuce.core.protocol.CommandKeyword;
import io.lettuce.core.protocol.CommandType;
import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.PendingMessage;
import org.springframework.data.redis.connection.stream.PendingMessages;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamInfo;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageStreamer {

  public static final String ERROR_COUNT_KEY = "error_count";
  private static final ObjectMapper objectMapper = new ObjectMapper();

  private final RedisTemplate<String, String> redisTemplate;

  public MessageStreamer(RedisTemplate<String, String> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  public Object getRedisValue(String key, String field) {
    return this.redisTemplate.opsForHash().get(key, field);
  }

  public void increaseRedisValue(String key, String field) {
    this.redisTemplate.opsForHash().increment(key, field, 1);
  }

  public void addToStream(String streamKey, MessageEvent message) {
    try {
      this.redisTemplate
          .opsForStream()
          .add(
              StreamRecords.newRecord()
                  .ofObject(objectMapper.writeValueAsString(message))
                  .withStreamKey(streamKey));

      log.info("Message added to stream: key: {} / mss: {}", streamKey, message);
    } catch (JsonProcessingException e) {
      log.error("Failed to serialize message to Json - key:{} / mss: {}", streamKey, message);
    }
  }

  public void ackStream(String consumerGroupName, ObjectRecord<String, String> message) {
    this.redisTemplate.opsForStream().acknowledge(consumerGroupName, message);
    log.info("Message responses ack: group: {} / mss: {}", consumerGroupName, message);
  }

  public void claimStream(PendingMessage pendingMessage, String consumerName) {
    RedisAsyncCommands commands =
        (RedisAsyncCommands)
            Objects.requireNonNull(this.redisTemplate.getConnectionFactory())
                .getConnection()
                .getNativeConnection();

    CommandArgs<String, String> args =
        new CommandArgs<>(StringCodec.UTF8)
            .add(pendingMessage.getIdAsString())
            .add(pendingMessage.getGroupName())
            .add(consumerName)
            .add("20")
            .add(pendingMessage.getIdAsString());

    commands.dispatch(CommandType.XCLAIM, new StatusOutput<>(StringCodec.UTF8), args);

    log.info(
        "Message claimed: id: {} / group: {} / consumer: {}",
        pendingMessage.getIdAsString(),
        pendingMessage.getGroupName(),
        consumerName);
  }

  public ObjectRecord<String, String> findStreamMessageById(String streamKey, String id) {
    List<ObjectRecord<String, String>> objectRecords =
        this.findStreamMessageByRange(streamKey, id, id);
    if (objectRecords.isEmpty()) {
      return null;
    }
    return objectRecords.getFirst();
  }

  public List<ObjectRecord<String, String>> findStreamMessageByRange(
      String streamKey, String startId, String endId) {
    return this.redisTemplate
        .opsForStream()
        .range(String.class, streamKey, Range.closed(startId, endId));
  }

  public void deleteFromStream(String streamKey, RecordId id) {
    this.redisTemplate.opsForStream().delete(streamKey, id);
    log.info("Message deleted: key: {} / id: {}", streamKey, id);
  }

  public void createStreamConsumerGroup(String streamKey, String consumerGroupName) {
    if (Boolean.TRUE.equals(this.redisTemplate.hasKey(streamKey))) {
      if (!isStreamConsumerGroupExist(streamKey, consumerGroupName)) {
        this.redisTemplate
            .opsForStream()
            .createGroup(streamKey, ReadOffset.from("0"), consumerGroupName);
        log.info("Consumer group created: key: {} / group: {}", streamKey, consumerGroupName);
      }
      log.info("Consumer group already exists: key: {} / group: {}", streamKey, consumerGroupName);
      return;
    }

    RedisAsyncCommands commands =
        (RedisAsyncCommands)
            Objects.requireNonNull(this.redisTemplate.getConnectionFactory())
                .getConnection()
                .getNativeConnection();

    CommandArgs<String, String> args =
        new CommandArgs<>(StringCodec.UTF8)
            .add(CommandKeyword.CREATE)
            .add(streamKey)
            .add(consumerGroupName)
            .add("0")
            .add("MKSTREAM");

    commands.dispatch(CommandType.XGROUP, new StatusOutput<>(StringCodec.UTF8), args);

    log.info("Consumer group created: key: {} / group: {}", streamKey, consumerGroupName);
  }

  public PendingMessages findStreamPendingMessages(
      String streamKey, String consumerGroupName, String consumerName) {
    return this.redisTemplate
        .opsForStream()
        .pending(
            streamKey, Consumer.from(consumerGroupName, consumerName), Range.unbounded(), 100L);
  }

  public void deleteFromPending(
      String streamKey, String consumerGroupName, String consumerName, RecordId id) {
    this.redisTemplate
        .opsForStream()
        .pending(streamKey, Consumer.from(consumerGroupName, consumerName), Range.unbounded(), 100L)
        .forEach(
            pendingMessage -> {
              if (Objects.equals(pendingMessage.getId(), id)) {
                this.redisTemplate.opsForStream().delete(streamKey, pendingMessage.getId());
              }
            });
  }

  public boolean isStreamConsumerGroupExist(String streamKey, String consumerGroupName) {
    Iterator<StreamInfo.XInfoGroup> iterator =
        this.redisTemplate.opsForStream().groups(streamKey).stream().iterator();

    while (iterator.hasNext()) {
      StreamInfo.XInfoGroup xInfoGroup = iterator.next();
      if (xInfoGroup.groupName().equals(consumerGroupName)) {
        return true;
      }
    }

    return false;
  }

  public StreamMessageListenerContainer createStreamMessageListenerContainer() {
    log.info("StreamMessageListenerContainer created");
    return StreamMessageListenerContainer.create(
        Objects.requireNonNull(this.redisTemplate.getConnectionFactory()),
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions.builder()
            .targetType(String.class)
            .pollTimeout(Duration.ofMillis(20))
            .build());
  }
}
