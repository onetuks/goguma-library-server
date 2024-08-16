package com.onetuks.librarydomain.member.service;

import static com.onetuks.libraryobject.enums.CacheName.RECOMMENDED_MEMBERS_CACHE_KEY;

import com.onetuks.librarydomain.book.model.Book;
import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.librarydomain.review.model.Review;
import com.onetuks.librarydomain.review.repository.ReviewRepository;
import com.onetuks.librarydomain.weekly.model.WeeklyFeaturedBook;
import com.onetuks.librarydomain.weekly.repository.WeeklyFeaturedBookRepository;
import com.onetuks.libraryobject.enums.CacheName;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.cache.annotation.Cacheable;
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

  @Cacheable(value = CacheName.RECOMMENDED_MEMBERS, key = RECOMMENDED_MEMBERS_CACHE_KEY)
  @Transactional(readOnly = true)
  public Set<Member> searchAllForRecommend() {
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

    Set<Member> recommendedMembers = Collections.synchronizedSet(new HashSet<>());
    recommendedMembers.addAll(bestQualityMembers);
    recommendedMembers.addAll(bestQuantityMembers);
    return recommendedMembers;
  }
}
