package com.onetuks.librarydomain;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

public class DomainArchitectureTest extends DomainIntegrationTest {

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
    @DisplayName("result 패키지 안에 있는 클래스는 Result 로 끝난다.")
    void result_ClassNamePostfix_Test() {
      ArchRule rule =
          ArchRuleDefinition.classes()
              .that()
              .resideInAnyPackage("..result..")
              .should()
              .haveSimpleNameEndingWith("Result");

      rule.check(javaClasses);
    }

    @Test
    @Disabled
    @DisplayName("param 패키지 안에 있는 클래스는 Param 로 끝난다.")
    void param_ClassNamePostfix_Test() {
      ArchRule rule =
          ArchRuleDefinition.classes()
              .that()
              .resideInAnyPackage("..param..")
              .should()
              .haveSimpleNameEndingWith("Param");

      rule.check(javaClasses);
    }

    @Test
    @DisplayName("service 패키지 안에 있는 클래스는 Service 로 끝난다.")
    void service_ClassNamePostfix_Test() {
      ArchRule rule =
          ArchRuleDefinition.classes()
              .that()
              .resideInAnyPackage("..service")
              .and()
              .resideOutsideOfPackage("..dto..")
              .and()
              .resideOutsideOfPackage("..vo..")
              .and()
              .resideOutsideOfPackage("..event..")
              .and()
              .resideOutsideOfPackage("..listener..")
              .should()
              .haveSimpleNameEndingWith("Service")
              .andShould()
              .beAnnotatedWith(Service.class)
              .orShould()
              .beAnnotatedWith(Component.class);

      rule.check(javaClasses);
    }

    @Test
    @Disabled
    @DisplayName("config 패키지 안에 있는 클래스는 Config 로 끝난다.")
    void config_ClassNamePostfix_Test() {
      ArchRule rule =
          ArchRuleDefinition.classes()
              .that()
              .resideInAnyPackage("..config..")
              .and()
              .doNotHaveSimpleName("AuthPermittedEndpoint")
              .should()
              .haveSimpleNameEndingWith("Config")
              .andShould()
              .beAnnotatedWith(Configuration.class);

      rule.check(javaClasses);
    }
  }

  @Nested
  class DependancyTest {

    @Test
    @DisplayName("Service는 Controller를 의존하면 안 된다")
    void service_NoDependOn_Test() {
      ArchRule rule =
          ArchRuleDefinition.noClasses()
              .that()
              .resideInAnyPackage("..service")
              .should()
              .dependOnClassesThat()
              .resideInAnyPackage("..controller");

      rule.check(javaClasses);
    }
  }
}
