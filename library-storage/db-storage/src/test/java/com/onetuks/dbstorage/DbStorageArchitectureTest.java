package com.onetuks.dbstorage;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class DbStorageArchitectureTest extends DbStorageIntegrationTest {

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
              .resideInAnyPackage("..repository")
              .should()
              .haveSimpleNameEndingWith("Repository");

      rule.check(javaClasses);
    }

    @Test
    @DisplayName("converter 패키지 안에 있는 클래스는 Converter 로 끝난다.")
    void converter_ClassNamePostfix_Test() {
      ArchRule rule =
          ArchRuleDefinition.classes()
              .that()
              .resideInAnyPackage("..converter")
              .should()
              .haveSimpleNameEndingWith("Converter");

      rule.check(javaClasses);
    }
  }

  @Nested
  class DependancyTest {

    @Test
    @DisplayName("Entity는 오직 Repository와 Converter에 의해서만 의존한다")
    void model_HaveDependancy_Test() {
      ArchRule rule =
          ArchRuleDefinition.classes()
              .that()
              .resideInAnyPackage("..entity")
              .should()
              .onlyHaveDependentClassesThat()
              .resideInAnyPackage(
                  "..repository..", "..converter..", "..entity..", "..vo..", "..fixture..");

      rule.check(javaClasses);
    }

    @Test
    @DisplayName("Entity 는 entity, vo, enum 이외에 아무것도 의존하지 않는다.")
    void model_NoDependOn_Test() {
      ArchRule rule =
          ArchRuleDefinition.classes()
              .that()
              .resideInAnyPackage("..entity")
              .and()
              .haveSimpleNameNotStartingWith("Q")
              .should()
              .onlyDependOnClassesThat()
              .resideInAnyPackage(
                  "java..",
                  "jakarta..",
                  "lombok..",
                  "..hibernate..",
                  "..entity..",
                  "..vo..",
                  "..enums..",
                  "..annotation..");

      rule.check(javaClasses);
    }
  }
}
