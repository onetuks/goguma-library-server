package com.onetuks.dbstorage.book.repository;

import static com.onetuks.dbstorage.book.entity.QBookEntity.bookEntity;

import com.onetuks.dbstorage.book.entity.BookEntity;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class BookEntityJpaQueryDslRepository {

  private final JPAQueryFactory queryFactory;

  public BookEntityJpaQueryDslRepository(JPAQueryFactory queryFactory) {
    this.queryFactory = queryFactory;
  }

  @Transactional(readOnly = true)
  public Page<BookEntity> findAllByIsPermitted(boolean inspectionMode, Pageable pageable) {
    List<BookEntity> content =
        queryFactory
            .selectFrom(bookEntity)
            .where(onlyNotPermitted(inspectionMode))
            .orderBy(bookEntity.bookId.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

    JPAQuery<Long> countQuery =
        queryFactory
            .select(bookEntity.count())
            .from(bookEntity)
            .where(onlyNotPermitted(inspectionMode));

    return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
  }

  private BooleanExpression onlyNotPermitted(boolean inspectionMode) {
    return inspectionMode ? bookEntity.isPermitted.isFalse() : null;
  }
}
