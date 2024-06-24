package com.onetuks.filestorage;

import com.onetuks.filestorage.FileStorageIntegrationTest.FileStorageConfig;
import com.onetuks.filestorage.FileStorageIntegrationTest.FileStorageIntegrationTestInitializer;
import com.onetuks.libraryobject.vo.TestFileCleaner;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;
import org.testcontainers.utility.DockerImageName;

@ActiveProfiles(value = "test")
@SpringBootTest(classes = FileStorageConfig.class)
@ContextConfiguration(initializers = FileStorageIntegrationTestInitializer.class)
public class FileStorageIntegrationTest {

  @Configuration
  @ComponentScan(
      basePackages = "com.onetuks.filestorage",
      basePackageClasses = TestFileCleaner.class)
  public static class FileStorageConfig {}

  static final LocalStackContainer localStack;

  @Autowired private TestFileCleaner testFileCleaner;

  @AfterEach
  void tearDown() {
    testFileCleaner.deleteAllTestStatic();
  }

  static {
    localStack =
        new LocalStackContainer(DockerImageName.parse("localstack/localstack"))
            .withServices(Service.S3)
            .withStartupTimeout(Duration.ofSeconds(600));
    localStack.start();
  }

  static class FileStorageIntegrationTestInitializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
      Map<String, String> properties = new HashMap<>();

      try {
        localStack.execInContainer("awslocal", "s3api", "create-bucket", "--bucket", "test-bucket");

        properties.put("aws.endpoint", String.valueOf(localStack.getEndpoint()));
        properties.put("aws.bucket-name", "test-bucket");
      } catch (Exception e) {
        log.info("aws test initialize failed");
      }

      TestPropertyValues.of(properties).applyTo(applicationContext);
    }
  }
}
