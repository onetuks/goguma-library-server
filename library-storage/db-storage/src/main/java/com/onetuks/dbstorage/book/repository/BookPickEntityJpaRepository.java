package com.onetuks.dbstorage.book.repository;

import com.onetuks.dbstorage.book.entity.BookPickEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookPickEntityJpaRepository extends JpaRepository<BookPickEntity, Long> {

  boolean existsByMemberEntityMemberIdAndBookEntityBookId(long memberId, long bookId);

  Page<BookPickEntity> findAllByMemberEntityMemberId(long memberId, Pageable pageable);
}
