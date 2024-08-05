package com.onetuks.librarypoint.repository.impl;

import com.onetuks.dbstorage.member.entity.MemberEntity;
import com.onetuks.dbstorage.member.repository.MemberEntityJpaRepository;
import com.onetuks.librarypoint.repository.PointRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class PointRepositoryImpl implements PointRepository {

  private final MemberEntityJpaRepository repository;

  public PointRepositoryImpl(MemberEntityJpaRepository repository) {
    this.repository = repository;
  }

  @Override
  @Transactional
  public void creditPoints(long memberId, long creditPoint) {
    validatePointValue(creditPoint);

    getMember(memberId).addPoints(creditPoint);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void creditPointsWithLock(long memberId, long creditPoint) {
    validatePointValue(creditPoint);

    repository
        .findByMemberId(memberId)
        .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 멤버입니다."))
        .addPoints(creditPoint);
  }

  @Override
  @Transactional
  public void debitPoints(long memberId, long debitPoint) {
    validatePointValue(debitPoint);

    getMember(memberId).minusPoints(debitPoint);
  }

  private void validatePointValue(long point) {
    if (point <= 0) {
      throw new IllegalArgumentException("포인트 변동값은 양수여야 합니다.");
    }
  }

  private MemberEntity getMember(long memberId) {
    return repository
        .findById(memberId)
        .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 멤버입니다."));
  }
}
