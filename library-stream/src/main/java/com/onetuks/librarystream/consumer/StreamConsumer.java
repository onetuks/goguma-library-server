package com.onetuks.librarystream.consumer;

import com.onetuks.librarystream.producer.event.MessageEvent;
import java.time.Duration;
import lombok.Getter;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;

@Getter
public abstract class StreamConsumer
    implements StreamListener<String, ObjectRecord<String, String>>,
    InitializingBean, DisposableBean {

  private StreamMessageListenerContainer<String, ObjectRecord<String, String>> listenerContainer;
  private Subscription subscription;

  private String streamKey;
  private String consumerGroupName;
  private String consumerName;

  @Override
  public void destroy() throws Exception {
    if (this.subscription != null) {
      this.subscription.cancel();
    }
    if (this.listenerContainer != null) {
      this.listenerContainer.stop();
    }
  }

  protected void setUpAndStartConsumer(
      String streamKey, String consumerGroupName, String consumerName,
      StreamMessageListenerContainer listenerContainer) throws InterruptedException {
    this.streamKey = streamKey;
    this.consumerGroupName = consumerGroupName;
    this.consumerName = consumerName;
    this.listenerContainer = listenerContainer;
    this.subscription = getListenerContainer()
        .receive(
            Consumer.from(this.getConsumerGroupName(), this.getConsumerName()),
            StreamOffset.create(this.getStreamKey(), ReadOffset.lastConsumed()),
            this);

    this.getSubscription().await(Duration.ofSeconds(2));
    this.getListenerContainer().start();
  }
}
