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

  Page<ReviewEntity> findAllByMemberEntityMemberId(long memberId, Pageable pageable);

  Page<ReviewEntity> findAllByBookEntityInOrderByPickCountDesc(
      List<BookEntity> thisWeekFeaturedBooks, Pageable pageable);

  @Query(
      value =
          """
      select r.memberEntity from ReviewEntity r
      where r.bookEntity in :thisWeekFeaturedBooks
      group by r.memberEntity
      order by count(r.memberEntity) desc
      """)
  Page<MemberEntity> findAllByWeeklyMostCountDesc(
      List<BookEntity> thisWeekFeaturedBooks, Pageable pageable);

  @Lock(value = LockModeType.PESSIMISTIC_WRITE)
  Optional<ReviewEntity> findByReviewId(long reviewId);
}
