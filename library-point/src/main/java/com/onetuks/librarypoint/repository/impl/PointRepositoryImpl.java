package com.onetuks.librarypoint.repository.impl;

import com.onetuks.dbstorage.member.entity.MemberEntity;
import com.onetuks.dbstorage.member.repository.MemberEntityJpaRepository;
import com.onetuks.librarypoint.repository.PointRepository;
import com.onetuks.librarypoint.repository.converter.PointHistoryConverter;
import com.onetuks.librarypoint.repository.entity.PointHistoryEntity;
import com.onetuks.librarypoint.service.model.PointHistory;
import com.onetuks.librarypoint.service.model.vo.Activity;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class PointRepositoryImpl implements PointRepository {

  private final MemberEntityJpaRepository memberEntityJpaRepository;
  private final PointHistoryEntityJpaRepository pointHistoryEntityJpaRepository;
  private final PointHistoryConverter pointHistoryConverter;

  public PointRepositoryImpl(
      MemberEntityJpaRepository repository,
      PointHistoryEntityJpaRepository pointHistoryEntityJpaRepository,
      PointHistoryConverter pointHistoryConverter) {
    this.memberEntityJpaRepository = repository;
    this.pointHistoryEntityJpaRepository = pointHistoryEntityJpaRepository;
    this.pointHistoryConverter = pointHistoryConverter;
  }

  @Override
  @Transactional
  public void creditPoints(long memberId, Activity activity) {
    MemberEntity memberEntity = getMember(memberId);
    memberEntity.addPoints(activity.getPoints());
    pointHistoryEntityJpaRepository.save(
        new PointHistoryEntity(
            null,
            memberEntity,
            activity.getDescription(),
            activity.getPoints()));
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void creditPointsWithLock(long memberId, Activity activity) {
    MemberEntity memberEntity = memberEntityJpaRepository
        .findByMemberId(memberId)
        .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 멤버입니다."));
    memberEntity.addPoints(activity.getPoints());
    pointHistoryEntityJpaRepository.save(
        new PointHistoryEntity(
            null,
            memberEntity,
            activity.getDescription(),
            activity.getPoints()));
  }

  @Override
  @Transactional
  public void debitPoints(long memberId, Activity activity) {
    MemberEntity memberEntity = getMember(memberId);
    memberEntity.minusPoints(activity.getPoints());
    pointHistoryEntityJpaRepository.save(
        new PointHistoryEntity(
            null,
            memberEntity,
            activity.getNegativeDescription(),
            activity.getNegativePoints()));
  }

  @Override
  public Page<PointHistory> readAllPointHistories(long memberId, Pageable pageable) {
    return pointHistoryEntityJpaRepository
        .findAllByMemberEntityMemberId(memberId, pageable)
        .map(pointHistoryConverter::toModel);
  }

  private MemberEntity getMember(long memberId) {
    return memberEntityJpaRepository
        .findById(memberId)
        .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 멤버입니다."));
  }
}
