package com.onetuks.librarypoint;

import com.onetuks.dbstorage.member.repository.MemberEntityJpaRepository;
import com.onetuks.libraryobject.config.RedisConfig;
import com.onetuks.librarypoint.CorePointIntegrationTest.CorePointConfig;
import com.onetuks.librarypoint.CorePointIntegrationTest.CorePointInitializer;
import com.onetuks.librarypoint.repository.DailyPointLimitRepository;
import com.onetuks.librarypoint.repository.PointRepository;
import com.onetuks.librarypoint.service.PointServiceImpl;
import com.redis.testcontainers.RedisContainer;
import java.util.HashMap;
import java.util.Map;
import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MySQLContainer;

@ActiveProfiles(value = "test")
@SpringBootTest(classes = CorePointConfig.class)
@ContextConfiguration(initializers = CorePointInitializer.class)
public class CorePointIntegrationTest {

  @Autowired public PointServiceImpl pointService;
  @Autowired public PointRepository pointRepository;
  @Autowired public DailyPointLimitRepository dailyPointLimitRepository;

  @Autowired public MemberEntityJpaRepository memberEntityJpaRepository;

  @Configuration
  @ComponentScan(
      basePackages = {"com.onetuks.librarypoint", "com.onetuks.dbstorage"},
      basePackageClasses = RedisConfig.class)
  public static class CorePointConfig {}

  private static final MySQLContainer<?> database;
  private static final Flyway flyway;
  private static final RedisContainer redis;

  static {
    database =
        new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("goguma-library-test")
            .withUsername("root")
            .withPassword("password");
    database.start();

    flyway =
        Flyway.configure()
            .dataSource(database.getJdbcUrl(), database.getUsername(), database.getPassword())
            .locations("filesystem:../library-storage/db-storage/migration/sql")
            .load();
    flyway.migrate();

    redis = new RedisContainer(RedisContainer.DEFAULT_IMAGE_NAME.withTag("7"));
    redis.start();
  }

  static class CorePointInitializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
      Map<String, String> properties = new HashMap<>();

      var localDbHost = database.getHost();
      var localDbPort = database.getFirstMappedPort();

      properties.put(
          "spring.datasource.url",
          "jdbc:mysql://" + localDbHost + ":" + localDbPort + "/goguma-library-test");

      var redisHost = redis.getHost();
      var redisPort = redis.getFirstMappedPort();

      properties.put("spring.data.redis.host", redisHost);
      properties.put("spring.data.redis.port", String.valueOf(redisPort));

      TestPropertyValues.of(properties).applyTo(applicationContext);
    }
  }
}
