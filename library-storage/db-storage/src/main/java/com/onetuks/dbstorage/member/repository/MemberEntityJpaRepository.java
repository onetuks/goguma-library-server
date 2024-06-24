package com.onetuks.dbstorage.member.repository;

import com.onetuks.dbstorage.member.entity.MemberEntity;
import com.onetuks.dbstorage.member.entity.MemberStaticsEntity;
import com.onetuks.libraryobject.enums.ClientProvider;
import java.util.Optional;
import javax.swing.text.html.Option;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberEntityJpaRepository extends JpaRepository<MemberEntity, Long> {

  Optional<MemberEntity> findByAuthInfoEmbeddableSocialIdAndAuthInfoEmbeddableClientProvider(
      String socialId, ClientProvider clientProvider);
}
