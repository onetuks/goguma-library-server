package com.onetuks.librarypoint.repository;

import com.onetuks.dbstorage.member.entity.MemberEntity;
import com.onetuks.dbstorage.member.repository.MemberEntityJpaRepository;
import com.onetuks.libraryobject.exception.NoSuchEntityException;
import com.onetuks.librarypoint.repository.converter.PointHistoryConverter;
import com.onetuks.librarypoint.repository.entity.PointHistoryEntity;
import com.onetuks.librarypoint.service.model.PointHistory;
import com.onetuks.librarypoint.service.model.vo.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class PointRepository {

  private final MemberEntityJpaRepository memberEntityJpaRepository;
  private final PointHistoryEntityJpaRepository pointHistoryEntityJpaRepository;
  private final PointHistoryConverter pointHistoryConverter;

  public PointRepository(
      MemberEntityJpaRepository repository,
      PointHistoryEntityJpaRepository pointHistoryEntityJpaRepository,
      PointHistoryConverter pointHistoryConverter) {
    this.memberEntityJpaRepository = repository;
    this.pointHistoryEntityJpaRepository = pointHistoryEntityJpaRepository;
    this.pointHistoryConverter = pointHistoryConverter;
  }

  @Transactional
  public void creditPoints(long memberId, Activity activity) {
    MemberEntity memberEntity = getMember(memberId);
    memberEntity.addPoints(activity.getPoints());
    pointHistoryEntityJpaRepository.save(
        new PointHistoryEntity(
            null, memberEntity, activity.getDescription(), activity.getPoints()));
  }

  @Transactional
  public void creditPointsWithLock(long memberId, Activity activity) {
    MemberEntity memberEntity =
        memberEntityJpaRepository
            .findByMemberId(memberId)
            .orElseThrow(() -> new NoSuchEntityException("존재하지 않는 멤버입니다."));
    memberEntity.addPoints(activity.getPoints());
    pointHistoryEntityJpaRepository.save(
        new PointHistoryEntity(
            null, memberEntity, activity.getDescription(), activity.getPoints()));
  }

  @Transactional
  public void debitPoints(long memberId, Activity activity) {
    MemberEntity memberEntity = getMember(memberId);
    memberEntity.minusPoints(activity.getPoints());
    pointHistoryEntityJpaRepository.save(
        new PointHistoryEntity(
            null, memberEntity, activity.getNegativeDescription(), activity.getNegativePoints()));
  }

  @Transactional(readOnly = true)
  public Page<PointHistory> readAllPointHistories(long memberId, Pageable pageable) {
    return pointHistoryEntityJpaRepository
        .findAllByMemberEntityMemberIdOrderByCreatedAtDesc(memberId, pageable)
        .map(pointHistoryConverter::toModel);
  }

  @Transactional(readOnly = true)
  public MemberEntity getMember(long memberId) {
    return memberEntityJpaRepository
        .findById(memberId)
        .orElseThrow(() -> new NoSuchEntityException("존재하지 않는 멤버입니다."));
  }

  @Transactional
  public void deleteAllPointHistories(long memberId) {
    pointHistoryEntityJpaRepository.deleteAllByMemberEntityMemberId(memberId);
  }
}
