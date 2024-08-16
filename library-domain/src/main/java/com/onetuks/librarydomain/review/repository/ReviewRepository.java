package com.onetuks.librarydomain.review.repository;

import com.onetuks.librarydomain.book.model.Book;
import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.librarydomain.review.model.Review;
import com.onetuks.libraryobject.enums.SortBy;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository {

  int BEST_QUALITY_MEMBER_COUNT = 7;
  int BEST_QUANTITY_MEMBER_COUNT = 3;

  Review create(Review review);

  Review read(long reviewId);

  Review readWithLock(long reviewId);

  Page<Review> readAll(SortBy sortBy, Pageable pageable);

  Page<Review> readAll(long bookId, SortBy sortBy, Pageable pageable);

  Page<Review> readAll(long memberId, Pageable pageable);

  Page<Review> readAll(List<Book> thisWeekInterestedCategoriesBooks, Pageable pageable);

  Page<Review> readAllWeeklyMostPicked(List<Book> thisWeekFeaturedBooks);

  Page<Member> readAllWeeklyMostWrite(List<Book> thisWeekFeaturedBooks);

  Review update(Review review);

  void delete(long reviewId);
}
