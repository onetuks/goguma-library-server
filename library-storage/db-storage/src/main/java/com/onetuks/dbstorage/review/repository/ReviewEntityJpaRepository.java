package com.onetuks.dbstorage.review.repository;

import com.onetuks.dbstorage.book.entity.BookEntity;
import com.onetuks.dbstorage.member.entity.MemberEntity;
import com.onetuks.dbstorage.review.entity.ReviewEntity;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface ReviewEntityJpaRepository extends JpaRepository<ReviewEntity, Long> {

  Page<ReviewEntity> findAllByBookEntityInOrderByPickCountDesc(
      List<BookEntity> thisWeekFeaturedBooks, Pageable pageable);

  @Query(
      value =
          """
      SELECT r.memberEntity FROM ReviewEntity r
        WHERE r.bookEntity IN :thisWeekFeaturedBooks
        GROUP BY r.memberEntity
        ORDER BY count(r.memberEntity) DESC
      """)
  Page<MemberEntity> findAllByWeeklyMostCountDesc(
      List<BookEntity> thisWeekFeaturedBooks, Pageable pageable);

  @Lock(value = LockModeType.PESSIMISTIC_WRITE)
  Optional<ReviewEntity> findByReviewId(long reviewId);

  Page<ReviewEntity> findAllByBookEntityIn(
      List<BookEntity> thisWeekInterestedCategoriesBooks, Pageable pageable);

  void deleteAllByMemberEntity(MemberEntity memberEntity);

  void deleteAllByBookEntity(BookEntity bookEntity);
}
