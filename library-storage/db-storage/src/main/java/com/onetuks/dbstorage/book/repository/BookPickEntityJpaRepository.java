package com.onetuks.dbstorage.book.repository;

import com.onetuks.dbstorage.book.entity.BookEntity;
import com.onetuks.dbstorage.book.entity.BookPickEntity;
import com.onetuks.dbstorage.member.entity.MemberEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookPickEntityJpaRepository extends JpaRepository<BookPickEntity, Long> {

  Optional<BookPickEntity> findByMemberEntityMemberIdAndBookEntityBookId(
      long memberId, long bookId);

  Page<BookPickEntity> findAllByMemberEntityMemberId(long memberId, Pageable pageable);

  void deleteAllByMemberEntity(MemberEntity memberEntity);

  void deleteAllByBookEntity(BookEntity bookEntity);
}
