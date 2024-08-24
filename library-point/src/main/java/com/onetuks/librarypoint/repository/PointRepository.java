package com.onetuks.librarypoint.repository;

import com.onetuks.librarypoint.service.model.PointHistory;
import com.onetuks.librarypoint.service.model.vo.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface PointRepository {

  void creditPoints(long memberId, Activity activity);

  void creditPointsWithLock(long memberId, Activity activity);

  void debitPoints(long memberId, Activity activity);

  Page<PointHistory> readAllPointHistories(long memberId, Pageable pageable);
}
