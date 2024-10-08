package com.onetuks.dbstorage;

import com.onetuks.dbstorage.DbStorageIntegrationTest.DbStorageConfig;
import com.onetuks.dbstorage.DbStorageIntegrationTest.DbStorageInitializer;
import com.onetuks.dbstorage.attendance.repository.AttendanceEntityRepository;
import com.onetuks.dbstorage.book.repository.BookEntityRepository;
import com.onetuks.dbstorage.book.repository.BookPickEntityRepository;
import com.onetuks.dbstorage.member.repository.FollowEntityRepository;
import com.onetuks.dbstorage.member.repository.MemberEntityRepository;
import com.onetuks.dbstorage.review.repository.ReviewEntityRepository;
import com.onetuks.dbstorage.review.repository.ReviewPickEntityRepository;
import com.onetuks.dbstorage.weekly.repository.WeeklyFeaturedBookEntityRepository;
import com.onetuks.libraryobject.util.TestFileCleaner;
import java.util.HashMap;
import java.util.Map;
import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
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

  @Autowired public MemberEntityRepository memberEntityRepository;
  @Autowired public BookEntityRepository bookEntityRepository;
  @Autowired public BookPickEntityRepository bookPickEntityRepository;
  @Autowired public ReviewEntityRepository reviewEntityRepository;
  @Autowired public ReviewPickEntityRepository reviewPickEntityRepository;
  @Autowired public WeeklyFeaturedBookEntityRepository weeklyFeaturedBookEntityRepository;
  @Autowired public FollowEntityRepository followEntityRepository;
  @Autowired public AttendanceEntityRepository attendanceEntityRepository;

  @Configuration
  @ComponentScan(basePackages = "com.onetuks.dbstorage", basePackageClasses = TestFileCleaner.class)
  public static class DbStorageConfig {}

  @Autowired private TestFileCleaner testFileCleaner;

  @AfterEach
  void tearDown() {
    testFileCleaner.deleteAllTestStatic();
  }

  private static final MySQLContainer<?> database;
  private static final Flyway flyway;

  static {
    database =
        new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("goguma_library_test")
            .withUsername("root")
            .withPassword("password");
    database.start();

    flyway =
        Flyway.configure()
            .dataSource(database.getJdbcUrl(), database.getUsername(), database.getPassword())
            .locations("filesystem:./src/main/resources/db/migration")
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
          "jdbc:mysql://" + localDbHost + ":" + localDbPort + "/goguma_library_test");
      properties.put("spring.datasource.username", database.getUsername());
      properties.put("spring.datasource.password", database.getPassword());

      TestPropertyValues.of(properties).applyTo(applicationContext);
    }
  }
}
