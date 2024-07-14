package com.onetuks.librarypoint.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface DailyPointLimitRepository {

  void save(long memberId, int creditCount);

  int find(long memberId);

  boolean isCreditable(long memberId);
}
