package com.onetuks.dbstorage.review.repository;

import static com.onetuks.dbstorage.review.entity.QReviewEntity.reviewEntity;

import com.onetuks.dbstorage.review.entity.ReviewEntity;
import com.onetuks.libraryobject.enums.ReviewSortBy;
import com.querydsl.core.types.OrderSpecifier;
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
  public Page<ReviewEntity> findAll(ReviewSortBy reviewSortBy, Pageable pageable) {
    List<ReviewEntity> content =
        queryFactory
            .selectFrom(reviewEntity)
            .orderBy(reviewOrderBy(reviewSortBy))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

    JPAQuery<Long> countQuery = queryFactory.select(reviewEntity.count()).from(reviewEntity);

    return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
  }

  private OrderSpecifier<?> reviewOrderBy(ReviewSortBy reviewSortBy) {
    return reviewSortBy == ReviewSortBy.LATEST
        ? reviewEntity.updatedAt.desc()
        : reviewEntity.pickCount.desc();
  }
}
