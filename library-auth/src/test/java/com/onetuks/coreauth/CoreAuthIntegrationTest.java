package com.onetuks.coreauth;

import com.onetuks.coreauth.CoreAuthIntegrationTest.CoreAuthConfig;
import com.onetuks.coreauth.CoreAuthIntegrationTest.CoreAuthIntegrationTestInitializer;
import com.onetuks.coredomain.member.dto.MemberAuthResult;
import com.onetuks.coredomain.member.model.vo.AuthInfo;
import com.redis.testcontainers.RedisContainer;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ActiveProfiles(value = "test")
@SpringBootTest(classes = CoreAuthConfig.class)
@ContextConfiguration(initializers = CoreAuthIntegrationTestInitializer.class)
public class CoreAuthIntegrationTest {

  @Configuration
  @ComponentScan(
      basePackages = "com.onetuks.coreauth",
      basePackageClasses = {AuthInfo.class, MemberAuthResult.class})
  public static class CoreAuthConfig {}

  static final RedisContainer redis;

  @BeforeAll
  static void beforeAll() {
    redis.start();
  }

  @AfterAll
  static void afterAll() {
    redis.stop();
  }

  static {
    redis = new RedisContainer(RedisContainer.DEFAULT_IMAGE_NAME.withTag("7"));
  }

  static class CoreAuthIntegrationTestInitializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
      Map<String, String> properties = new HashMap<>();

      var redistHost = redis.getHost();
      var redistPort = redis.getFirstMappedPort();

      properties.put("spring.data.redis.host", redistHost);
      properties.put("spring.data.redis.port", String.valueOf(redistPort));

      TestPropertyValues.of(properties).applyTo(applicationContext);
    }
  }
}
