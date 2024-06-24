package com.onetuks.dbstorage;

import com.onetuks.dbstorage.DbStorageIntegrationTest.DbStorageConfig;
import com.onetuks.dbstorage.DbStorageIntegrationTest.DbStorageInitializer;
import com.onetuks.dbstorage.member.repository.MemberEntityRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;

@ActiveProfiles(value = "test")
@Transactional
@SpringBootTest(classes = DbStorageConfig.class)
@ContextConfiguration(initializers = DbStorageInitializer.class)
public class DbStorageIntegrationTest {

  @Autowired
  public MemberEntityRepository memberEntityRepository;

  @Configuration
  @ComponentScan(basePackages = "com.onetuks.dbstorage")
  public static class DbStorageConfig {}

  private static final MySQLContainer<?> database;
  private static final Flyway flyway;

  static {
    database = new MySQLContainer<>("mysql:8.0")
        .withDatabaseName("goguma-library-test")
        .withUsername("root")
        .withPassword("password");
    database.start();

    flyway = Flyway.configure()
        .dataSource(database.getJdbcUrl(), database.getUsername(), database.getPassword())
        .locations("filesystem:migration/sql")
        .load();
    flyway.migrate();
  }

  static class DbStorageInitializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
      Map<String, String> properties = new HashMap<>();

      var localDbHost = database.getHost();
      var localDbPort = database.getFirstMappedPort();

      properties.put(
          "spring.datasource.url",
          "jdbc:mysql://" + localDbHost + ":" + localDbPort + "/goguma-library-test");

      TestPropertyValues.of(properties).applyTo(applicationContext);
    }
  }
}
