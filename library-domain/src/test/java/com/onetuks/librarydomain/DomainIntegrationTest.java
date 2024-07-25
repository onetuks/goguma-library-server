package com.onetuks.librarydomain;

import com.onetuks.librarydomain.DomainIntegrationTest.DomainConfig;
import com.onetuks.librarydomain.attendance.repository.AttendanceRepository;
import com.onetuks.librarydomain.attendance.service.AttendanceService;
import com.onetuks.librarydomain.book.repository.BookPickRepository;
import com.onetuks.librarydomain.book.repository.BookRepository;
import com.onetuks.librarydomain.book.service.BookPickService;
import com.onetuks.librarydomain.book.service.BookService;
import com.onetuks.librarydomain.book.service.IsbnSearchService;
import com.onetuks.librarydomain.global.file.repository.FileRepository;
import com.onetuks.librarydomain.global.point.service.PointService;
import com.onetuks.librarydomain.member.repository.FollowRepository;
import com.onetuks.librarydomain.member.repository.MemberRepository;
import com.onetuks.librarydomain.member.service.FollowService;
import com.onetuks.librarydomain.member.service.MemberFacadeService;
import com.onetuks.librarydomain.member.service.MemberService;
import com.onetuks.librarydomain.review.repository.ReviewPickRepository;
import com.onetuks.librarydomain.review.repository.ReviewRepository;
import com.onetuks.librarydomain.review.service.ReviewPickService;
import com.onetuks.librarydomain.review.service.ReviewService;
import com.onetuks.librarydomain.weekly.repository.WeeklyFeaturedBookRepository;
import com.onetuks.librarydomain.weekly.service.WeeklyFeaturedBookService;
import com.onetuks.libraryobject.component.TestFileCleaner;
import com.onetuks.libraryobject.util.URIBuilder;
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
  @Autowired public BookPickService bookPickService;
  @Autowired public ReviewService reviewService;
  @Autowired public ReviewPickService reviewPickService;
  @Autowired public IsbnSearchService isbnSearchService;
  @Autowired public WeeklyFeaturedBookService weeklyFeaturedBookService;
  @Autowired public MemberFacadeService memberFacadeService;
  @Autowired public FollowService followService;
  @Autowired public AttendanceService attendanceService;

  @MockBean public FileRepository fileRepository;
  @MockBean public MemberRepository memberRepository;
  @MockBean public BookRepository bookRepository;
  @MockBean public BookPickRepository bookPickRepository;
  @MockBean public ReviewRepository reviewRepository;
  @MockBean public ReviewPickRepository reviewPickRepository;
  @MockBean public WeeklyFeaturedBookRepository weeklyFeaturedBookRepository;
  @MockBean public FollowRepository followRepository;
  @MockBean public AttendanceRepository attendanceRepository;

  @MockBean public PointService pointService;

  @Autowired private TestFileCleaner testFileCleaner;

  @Configuration
  @ComponentScan(
      basePackages = "com.onetuks.librarydomain",
      basePackageClasses = {TestFileCleaner.class, URIBuilder.class})
  public static class DomainConfig {}

  @AfterEach
  void tearDown() {
    testFileCleaner.deleteAllTestStatic();
  }
}
