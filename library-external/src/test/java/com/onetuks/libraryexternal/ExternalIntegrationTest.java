package com.onetuks.libraryexternal;

import com.onetuks.libraryexternal.ExternalIntegrationTest.ExternalConfig;
import com.onetuks.libraryexternal.book.service.IsbnSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(value = "test")
@SpringBootTest(classes = ExternalConfig.class)
public class ExternalIntegrationTest {

  @Autowired public IsbnSearchService isbnSearchService;

  @Configuration
  @ComponentScan(basePackages = "com.onetuks.libraryexternal")
  public static class ExternalConfig {}
}
