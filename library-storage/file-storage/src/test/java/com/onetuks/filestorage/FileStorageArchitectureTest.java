package com.onetuks.filestorage;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FileStorageArchitectureTest extends FileStorageIntegrationTest {

  JavaClasses javaClasses;

  @BeforeEach
  void setUp() {
    javaClasses =
        new ClassFileImporter()
            .withImportOption(new ImportOption.DoNotIncludeTests()) // 테스트 클래스는 이 검증에서 제외
            .importPackages(getClass().getPackageName());
  }

  @Nested
  class ClassNameTest {

    @Test
    @DisplayName("repository 패키지 안에 있는 클래스는 Repository 로 끝난다.")
    void repository_ClassNamePostfix_Test() {
      ArchRule rule =
          ArchRuleDefinition.classes()
              .that()
              .resideInAnyPackage("..repository..")
              .should()
              .haveSimpleNameEndingWith("Repository");

      rule.check(javaClasses);
    }
  }

  @Test
  @DisplayName("config 패키지 안에 있는 클래스는 Config 로 끝난다.")
  void config_ClassNamePostfix_Test() {
    ArchRule rule =
        ArchRuleDefinition.classes()
            .that()
            .resideInAnyPackage("..config..")
            .should()
            .haveSimpleNameEndingWith("Config");

    rule.check(javaClasses);
  }
}
