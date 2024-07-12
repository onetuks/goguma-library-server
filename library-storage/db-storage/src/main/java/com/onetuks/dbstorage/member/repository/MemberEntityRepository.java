package com.onetuks.dbstorage.member.repository;

import com.onetuks.dbstorage.member.converter.MemberConverter;
import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.librarydomain.member.repository.MemberRepository;
import com.onetuks.libraryobject.enums.ClientProvider;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class MemberEntityRepository implements MemberRepository {

  private final MemberEntityJpaRepository repository;
  private final MemberConverter converter;

  public MemberEntityRepository(MemberEntityJpaRepository repository, MemberConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }

  @Override
  public Member create(Member member) {
    return converter.toDomain(repository.save(converter.toEntity(member)));
  }

  @Override
  public Member read(long memberId) {
    return converter.toDomain(
        repository
            .findById(memberId)
            .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 멤버입니다.")));
  }

  @Override
  public Optional<Member> read(String social, ClientProvider clientProvider) {
    return repository
        .findByAuthInfoEmbeddableSocialIdAndAuthInfoEmbeddableClientProvider(social, clientProvider)
        .map(converter::toDomain);
  }

  // todo 멤버 통계 정보가 업데이트 되지 않는 현상 수정 (프로필 수정 시 멤버 통계 정보 수정되지 않도록 해야함)
  @Override
  public Member update(Member member) {
    return converter.toDomain(
        repository.save(
            converter.toEntity(
                member,
                repository
                    .findById(member.memberId())
                    .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 멤버입니다."))
                    .getMemberStaticsEntity())));
  }

  @Override
  public void delete(long memberId) {
    repository.deleteById(memberId);
  }
}
