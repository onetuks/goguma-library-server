package com.onetuks.librarydomain;

import com.onetuks.librarydomain.DomainIntegrationTest.DomainConfig;
import com.onetuks.librarydomain.book.repository.BookRepository;
import com.onetuks.librarydomain.book.service.BookService;
import com.onetuks.librarydomain.file.FileRepository;
import com.onetuks.librarydomain.member.repository.MemberRepository;
import com.onetuks.librarydomain.member.repository.PointRepository;
import com.onetuks.librarydomain.member.service.MemberService;
import com.onetuks.libraryobject.vo.TestFileCleaner;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(value = "test")
@SpringBootTest(classes = DomainConfig.class)
public class DomainIntegrationTest {

  @Autowired public MemberService memberService;
  @Autowired public BookService bookService;

  @MockBean public FileRepository fileRepository;
  @MockBean public PointRepository pointRepository;
  @MockBean public MemberRepository memberRepository;
  @MockBean public BookRepository bookRepository;

  @Autowired private TestFileCleaner testFileCleaner;

  @Configuration
  @ComponentScan(
      basePackages = "com.onetuks.librarydomain",
      basePackageClasses = TestFileCleaner.class)
  public static class DomainConfig {}

  @AfterEach
  void tearDown() {
    testFileCleaner.deleteAllTestStatic();
  }
}
