package com.onetuks.coreauth;

import com.onetuks.coreauth.CoreAuthIntegrationTest.CoreAuthConfig;
import com.onetuks.coreauth.CoreAuthIntegrationTest.CoreAuthIntegrationTestInitializer;
import com.onetuks.coreauth.oauth.strategy.GoogleClientProviderStrategy;
import com.onetuks.coreauth.service.AuthService;
import com.onetuks.coreauth.service.OAuth2ClientService;
import com.onetuks.librarydomain.member.service.MemberService;
import com.redis.testcontainers.RedisContainer;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

  @Autowired public AuthService authService;
  @Autowired public OAuth2ClientService oAuth2ClientService;

  @MockBean public GoogleClientProviderStrategy googleClientProviderStrategy;
  @MockBean public MemberService memberService;

  @Configuration
  @ComponentScan(basePackages = "com.onetuks.coreauth")
  public static class CoreAuthConfig {}

  static final RedisContainer redis;

  static {
    redis = new RedisContainer(RedisContainer.DEFAULT_IMAGE_NAME.withTag("7"));
    redis.start();
  }

  static class CoreAuthIntegrationTestInitializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
      Map<String, String> properties = new HashMap<>();

      var redisHost = redis.getHost();
      var redisPort = redis.getFirstMappedPort();

      properties.put("spring.data.redis.host", redisHost);
      properties.put("spring.data.redis.port", String.valueOf(redisPort));

      TestPropertyValues.of(properties).applyTo(applicationContext);
    }
  }
}
