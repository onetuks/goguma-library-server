package com.onetuks.dbstorage.member.repository;

import com.onetuks.dbstorage.member.entity.MemberEntity;
import com.onetuks.libraryobject.enums.ClientProvider;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface MemberEntityJpaRepository extends JpaRepository<MemberEntity, Long> {

  Optional<MemberEntity> findByAuthInfoEmbeddableSocialIdAndAuthInfoEmbeddableClientProvider(
      String socialId, ClientProvider clientProvider);

  @Lock(value = LockModeType.PESSIMISTIC_WRITE)
  Optional<MemberEntity> findByMemberId(long memberId);
}
