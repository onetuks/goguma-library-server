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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberFacadeService {

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
        weeklyFeaturedBookRepository.readAllForThisWeek().getContent().stream()
            .map(WeeklyFeaturedBook::book)
            .toList();

    List<Member> bestQualityMembers =
        reviewRepository.readAllWeeklyMostPicked(thisWeekFeaturedBooks).getContent().stream()
            .map(Review::member)
            .toList();

    List<Member> bestQuantityMembers =
        reviewRepository.readAllWeeklyMostWrite(thisWeekFeaturedBooks).getContent();

    List<Member> recommendedMembers = Collections.synchronizedList(new ArrayList<>());
    recommendedMembers.addAll(bestQualityMembers);
    recommendedMembers.addAll(bestQuantityMembers);
    return recommendedMembers;
  }
}
