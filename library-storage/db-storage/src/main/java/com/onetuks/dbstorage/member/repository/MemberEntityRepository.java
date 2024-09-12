package com.onetuks.dbstorage.member.repository;

import com.onetuks.dbstorage.common.DeletionRepository;
import com.onetuks.dbstorage.member.converter.MemberConverter;
import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.librarydomain.member.repository.MemberRepository;
import com.onetuks.libraryobject.enums.ClientProvider;
import com.onetuks.libraryobject.exception.NoSuchEntityException;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class MemberEntityRepository implements MemberRepository {

  private final MemberEntityJpaRepository repository;
  private final DeletionRepository deletionRepository;
  private final MemberConverter converter;

  public MemberEntityRepository(
      MemberEntityJpaRepository repository,
      DeletionRepository deletionRepository,
      MemberConverter converter) {
    this.repository = repository;
    this.deletionRepository = deletionRepository;
    this.converter = converter;
  }

  @Override
  public Member create(Member member) {
    return converter.toModel(repository.save(converter.toEntity(member)));
  }

  @Override
  public Member read(long memberId) {
    return converter.toModel(
        repository
            .findById(memberId)
            .orElseThrow(() -> new NoSuchEntityException("존재하지 않는 멤버입니다.")));
  }

  @Override
  public Optional<Member> read(String social, ClientProvider clientProvider) {
    return repository
        .findByAuthInfoEmbeddableSocialIdAndAuthInfoEmbeddableClientProvider(social, clientProvider)
        .map(converter::toModel);
  }

  @Override
  public Member update(Member member) {
    return converter.toModel(repository.save(converter.toEntity(member)));
  }

  @Override
  public void delete(long memberId) {
    deletionRepository.deleteMember(memberId);
  }
}
