package com.onetuks.dbstorage.member.repository;

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
    repository
        .findById(memberId)
        .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 멤버입니다."))
        .addPoints(point);
  }
}
