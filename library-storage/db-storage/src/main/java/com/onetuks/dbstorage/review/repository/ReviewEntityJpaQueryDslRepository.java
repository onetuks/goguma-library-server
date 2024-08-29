package com.onetuks.dbstorage.review.repository;

import static com.onetuks.dbstorage.review.entity.QReviewEntity.reviewEntity;
import static com.onetuks.dbstorage.review.entity.QReviewPickEntity.reviewPickEntity;

import com.onetuks.dbstorage.review.entity.ReviewEntity;
import com.onetuks.libraryobject.enums.SortBy;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
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
  public Page<ReviewEntity> findAllOrderByLatest(Pageable pageable) {
    List<ReviewEntity> content =
        queryFactory
            .selectFrom(reviewEntity)
            .orderBy(reviewEntity.updatedAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

    JPAQuery<Long> countQuery = queryFactory.select(reviewEntity.count()).from(reviewEntity);

    return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
  }

  @Transactional(readOnly = true)
  public Page<ReviewEntity> findAllOrderByPickCountDesc(Pageable pageable) {
    List<ReviewEntity> content =
        queryFactory
            .selectFrom(reviewEntity)
            .leftJoin(reviewPickEntity)
            .on(reviewEntity.reviewId.eq(reviewPickEntity.reviewEntity.reviewId))
            .where(reviewPickedAfterLastMondayMidnight())
            .groupBy(reviewEntity.reviewId)
            .orderBy(reviewOrderByPickCountDesc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .distinct()
            .fetch();

    JPAQuery<Long> countQuery =
        queryFactory
            .select(reviewEntity.count())
            .from(reviewEntity)
            .leftJoin(reviewPickEntity)
            .on(reviewEntity.reviewId.eq(reviewPickEntity.reviewEntity.reviewId))
            .where(reviewPickedAfterLastMondayMidnight())
            .groupBy(reviewEntity.reviewId)
            .orderBy(reviewOrderByPickCountDesc());

    return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
  }

  @Transactional(readOnly = true)
  public Page<ReviewEntity> findAllByBookId(long bookId, SortBy sortBy, Pageable pageable) {
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

  @Transactional(readOnly = true)
  public Page<ReviewEntity> findAllByMemberId(long memberId, SortBy sortBy, Pageable pageable) {
    List<ReviewEntity> content =
        queryFactory
            .selectFrom(reviewEntity)
            .where(equalsToMemberId(memberId))
            .orderBy(reviewOrderBy(sortBy))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

    JPAQuery<Long> countQuery =
        queryFactory
            .select(reviewEntity.count())
            .where(equalsToMemberId(memberId))
            .from(reviewEntity);

    return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
  }

  private BooleanExpression equalsToBookId(long bookId) {
    return reviewEntity.bookEntity.bookId.eq(bookId);
  }

  private BooleanExpression equalsToMemberId(long memberId) {
    return reviewEntity.memberEntity.memberId.eq(memberId);
  }

  private BooleanExpression reviewPickedAfterLastMondayMidnight() {
    LocalDateTime lastMondayMidnight =
        LocalDateTime.now()
            .minusWeeks(1L)
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            .toLocalDate()
            .atStartOfDay();

    return reviewPickEntity.createdAt.goe(lastMondayMidnight);
  }

  private OrderSpecifier<?> reviewOrderByPickCountDesc() {
    return reviewPickEntity.reviewEntity.reviewId.count().desc();
  }

  private OrderSpecifier<?> reviewOrderBy(SortBy sortBy) {
    return sortBy == SortBy.LATEST ? reviewEntity.updatedAt.desc() : reviewEntity.pickCount.desc();
  }
}
