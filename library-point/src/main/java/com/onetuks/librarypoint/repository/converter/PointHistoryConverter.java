package com.onetuks.librarypoint.repository.converter;

import com.onetuks.dbstorage.member.converter.MemberConverter;
import com.onetuks.librarypoint.repository.entity.PointHistoryEntity;
import com.onetuks.librarypoint.service.model.PointHistory;
import org.springframework.stereotype.Component;

@Component
public class PointHistoryConverter {

  private final MemberConverter memberConverter;

  public PointHistoryConverter(MemberConverter memberConverter) {
    this.memberConverter = memberConverter;
  }

  public PointHistory toModel(PointHistoryEntity entity) {
    return new PointHistory(
        entity.getPointHistoryId(),
        memberConverter.toModel(entity.getMemberEntity()),
        entity.getActivity(),
        entity.getPoints(),
        entity.getCreatedAt());
  }
}
