package com.onetuks.dbstorage.review.repository;

import com.onetuks.dbstorage.book.entity.BookEntity;
import com.onetuks.dbstorage.member.entity.MemberEntity;
import com.onetuks.dbstorage.review.entity.ReviewEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
