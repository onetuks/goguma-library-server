package com.onetuks.dbstorage.review.repository;

import static com.onetuks.dbstorage.review.entity.QReviewEntity.reviewEntity;

import com.onetuks.dbstorage.review.entity.ReviewEntity;
import com.onetuks.libraryobject.enums.SortBy;
import com.querydsl.core.types.OrderSpecifier;
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
public class ReviewEntityJpaQueryDslRepository {

  private final JPAQueryFactory queryFactory;

  public ReviewEntityJpaQueryDslRepository(JPAQueryFactory queryFactory) {
    this.queryFactory = queryFactory;
  }

  @Transactional(readOnly = true)
  public Page<ReviewEntity> findAll(SortBy sortBy, Pageable pageable) {
    List<ReviewEntity> content =
        queryFactory
            .selectFrom(reviewEntity)
            .orderBy(reviewOrderBy(sortBy))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

    JPAQuery<Long> countQuery = queryFactory.select(reviewEntity.count()).from(reviewEntity);

    return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
  }

  @Transactional(readOnly = true)
  public Page<ReviewEntity> findAll(long bookId, SortBy sortBy, Pageable pageable) {
    List<ReviewEntity> content =
        queryFactory
            .selectFrom(reviewEntity)
            .where(equalsToBookId(bookId))
            .orderBy(reviewOrderBy(sortBy))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

    JPAQuery<Long> countQuery =
        queryFactory.select(reviewEntity.count()).where(equalsToBookId(bookId)).from(reviewEntity);

    return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
  }

  private BooleanExpression equalsToBookId(long bookId) {
    return reviewEntity.bookEntity.bookId.eq(bookId);
  }

  private OrderSpecifier<?> reviewOrderBy(SortBy sortBy) {
    return sortBy == SortBy.LATEST ? reviewEntity.updatedAt.desc() : reviewEntity.pickCount.desc();
  }
}
