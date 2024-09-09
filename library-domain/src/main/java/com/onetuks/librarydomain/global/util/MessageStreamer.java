package com.onetuks.librarydomain.global.util;

import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageStreamer {

  private final RedisTemplate<Long, Object> longObjectRedisTemplate;

  public MessageStreamer(RedisTemplate<Long, Object> longObjectRedisTemplate) {
    this.longObjectRedisTemplate = longObjectRedisTemplate;
  }

  public void sendMessage(long streamKey, Object message) {
    longObjectRedisTemplate
        .opsForStream()
        .add(StreamRecords.newRecord().ofObject(message).withStreamKey(streamKey));
  }
}
