package com.onetuks.librarydomain.member.service;

import com.onetuks.librarydomain.book.model.Book;
import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.librarydomain.review.model.Review;
import com.onetuks.librarydomain.review.repository.ReviewRepository;
import com.onetuks.librarydomain.weekly.model.WeeklyFeaturedBook;
import com.onetuks.librarydomain.weekly.repository.WeeklyFeaturedBookRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberFacadeService {

  private static final int RECOMMEND_MEMBER_COUNT = 10;
  private static final int BEST_QUALITY_MEMBER_COUNT = 7;
  private static final int BEST_QUANTITY_MEMBER_COUNT = 3;

  private final ReviewRepository reviewRepository;
  private final WeeklyFeaturedBookRepository weeklyFeaturedBookRepository;

  public MemberFacadeService(
      ReviewRepository reviewRepository,
      WeeklyFeaturedBookRepository weeklyFeaturedBookRepository) {
    this.reviewRepository = reviewRepository;
    this.weeklyFeaturedBookRepository = weeklyFeaturedBookRepository;
  }

  @Transactional(readOnly = true)
  public List<Member> searchAllForRecommend() {
    List<Book> thisWeekFeaturedBooks =
        weeklyFeaturedBookRepository
            .readAllForThisWeek(PageRequest.of(0, RECOMMEND_MEMBER_COUNT))
            .getContent()
            .stream()
            .map(WeeklyFeaturedBook::book)
            .toList();

    List<Member> bestQualityMembers =
        reviewRepository
            .readAllWeeklyMostPicked(
                thisWeekFeaturedBooks, PageRequest.of(0, BEST_QUALITY_MEMBER_COUNT))
            .getContent()
            .stream()
            .map(Review::member)
            .toList();

    List<Member> bestQuantityMembers =
        reviewRepository
            .readAllWeeklyMostWrite(
                thisWeekFeaturedBooks, PageRequest.of(0, BEST_QUANTITY_MEMBER_COUNT))
            .getContent();

    List<Member> recommendedMembers = Collections.synchronizedList(new ArrayList<>());
    recommendedMembers.addAll(bestQualityMembers);
    recommendedMembers.addAll(bestQuantityMembers);
    return recommendedMembers;
  }
}
