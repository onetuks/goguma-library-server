package com.onetuks.libraryapi;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

public class ApiArchitectureTest {

  JavaClasses javaClasses;

  @BeforeEach
  void setUp() {
    javaClasses =
        new ClassFileImporter()
            .withImportOption(new ImportOption.DoNotIncludeTests())
            .importPackages(getClass().getPackageName());
  }

  @Nested
  class ClassNameTest {

    @Test
    @DisplayName("controller 패키지 안에 있는 클래스는 Controller 로 끝난다.")
    void controller_ClassNamePostfix_Test() {
      ArchRule rule =
          ArchRuleDefinition.classes()
              .that()
              .resideInAnyPackage("..controller")
              .should()
              .haveSimpleNameEndingWith("Controller")
              .andShould()
              .beAnnotatedWith(RestController.class)
              .orShould()
              .beAnnotatedWith(RequestMapping.class);

      rule.check(javaClasses);
    }

    @Test
    @DisplayName("request 패키지 안에 있는 클래스는 Request 로 끝난다.")
    void request_ClassNamePostfix_Test() {
      ArchRule rule =
          ArchRuleDefinition.classes()
              .that()
              .resideInAnyPackage("..request")
              .should()
              .haveSimpleNameEndingWith("Request");

      rule.check(javaClasses);
    }

    @Test
    @DisplayName("response 패키지 안에 있는 클래스는 Response 로 끝난다.")
    void response_ClassNamePostfix_Test() {
      ArchRule rule =
          ArchRuleDefinition.classes()
              .that()
              .resideInAnyPackage("..response")
              .should()
              .haveSimpleNameEndingWith("Response")
              .orShould()
              .haveSimpleNameEndingWith("Responses");

      rule.check(javaClasses);
    }
  }

  @Nested
  class MethodNameTest {

    @Test
    @DisplayName("Controller 에서는 get, post, patch, put, delete 로 시작하는 메서드 이름을 사용한다.")
    void controller_MethodNamePrefix_Test() {
      ArchRule rule =
          ArchRuleDefinition.methods()
              .that()
              .areDeclaredInClassesThat()
              .resideInAPackage("..controller")
              .should()
              .haveNameMatching("^(get|post|patch|put|delete).+");

      rule.check(javaClasses);
    }
  }

  @Nested
  class DependencyTest {

    @Test
    @DisplayName("Controller 는 Service 만 의존한다.")
    void controller_DependOn_Test() {
      ArchRule rule =
          ArchRuleDefinition.classes()
              .that()
              .resideInAPackage("..controller")
              .should()
              .onlyDependOnClassesThat()
              .resideInAnyPackage(
                  "..service",
                  "..dto..",
                  "..model",
                  "..util..",
                  "..enums..",
                  "..springframework..",
                  "jakarta..",
                  "java..");

      rule.check(javaClasses);
    }
  }
}
