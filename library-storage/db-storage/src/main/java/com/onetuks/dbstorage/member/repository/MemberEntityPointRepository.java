package com.onetuks.dbstorage.member.repository;

import com.onetuks.dbstorage.member.entity.MemberEntity;
import com.onetuks.librarydomain.member.repository.PointRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public class MemberEntityPointRepository implements PointRepository {

  private final MemberEntityJpaRepository repository;

  public MemberEntityPointRepository(MemberEntityJpaRepository repository) {
    this.repository = repository;
  }

  @Override
  public void creditPoints(long memberId, long point) {
    validatePointValue(point);

    getMember(memberId).addPoints(point);
  }

  @Override
  public void debitPoints(long memberId, long point) {
    validatePointValue(point);

    getMember(memberId).minusPoints(point);
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
