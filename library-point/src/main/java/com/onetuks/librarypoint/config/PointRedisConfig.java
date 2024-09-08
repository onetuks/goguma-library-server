package com.onetuks.librarypoint.config;

import com.onetuks.librarypoint.consumer.PointEventConsumer;
import com.onetuks.librarypoint.repository.DailyPointLimitRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;

@Configuration
@EnableRedisRepositories(basePackageClasses = DailyPointLimitRepository.class)
public class PointRedisConfig {

  @Bean
  public Subscription pointEventSubscription(
      RedisConnectionFactory connectionFactory, PointEventConsumer consumer) {
    StreamMessageListenerContainer<String, MapRecord<String, String, String>> listenerContainer =
        StreamMessageListenerContainer.create(connectionFactory);

    Subscription subscription =
        listenerContainer.receive(StreamOffset.fromStart("points_stream"), consumer);

    listenerContainer.start();
    return subscription;
  }
}
