package com.onetuks.librarydomain.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

import com.onetuks.librarydomain.BookFixture;
import com.onetuks.librarydomain.DomainIntegrationTest;
import com.onetuks.librarydomain.MemberFixture;
import com.onetuks.librarydomain.ReviewFixture;
import com.onetuks.librarydomain.WeeklyFeaturedBookFixture;
import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.librarydomain.review.model.Review;
import com.onetuks.librarydomain.weekly.model.WeeklyFeaturedBook;
import com.onetuks.libraryobject.enums.RoleType;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

class MemberFacadeServiceTest extends DomainIntegrationTest {

  @Test
  @DisplayName("추천 멤버를 10명 조회한다.")
  void searchAllForRecommend_10Member_Test() {
    // Given
    int count = 10;
    Page<WeeklyFeaturedBook> weeklyFeaturedBooks =
        new PageImpl<>(
            IntStream.range(0, count)
                .mapToObj(
                    i -> WeeklyFeaturedBookFixture.create((long) i, BookFixture.create((long) i)))
                .toList());
    Page<Review> weeklyMostPickedReviews =
        new PageImpl<>(
            IntStream.range(0, 7)
                .mapToObj(
                    i ->
                        ReviewFixture.create(
                            (long) i,
                            MemberFixture.create((long) i, RoleType.USER),
                            weeklyFeaturedBooks.getContent().get(i).book()))
                .toList());
    Page<Member> weeklyMostWriteMembers =
        new PageImpl<>(
            IntStream.range(0, 3)
                .mapToObj(
                    i ->
                        MemberFixture.create(
                            (long) i + weeklyMostPickedReviews.getTotalElements(), RoleType.USER))
                .toList());

    given(weeklyFeaturedBookRepository.readAllForThisWeek(any(PageRequest.class)))
        .willReturn(weeklyFeaturedBooks);
    given(reviewRepository.readAllWeeklyMostPicked(anyList(), any(PageRequest.class)))
        .willReturn(weeklyMostPickedReviews);
    given(reviewRepository.readAllWeeklyMostWrite(anyList(), any(PageRequest.class)))
        .willReturn(weeklyMostWriteMembers);

    // When
    List<Member> results = memberFacadeService.searchAllForRecommend();

    // Then
    assertThat(results).hasSize(count);
  }
}
