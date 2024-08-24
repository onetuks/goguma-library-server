package com.onetuks.librarypoint.service.model;

import com.onetuks.librarydomain.member.model.Member;
import java.time.LocalDateTime;

public record PointHistory(
    Long pointHistoryId,
    Member member,
    String activity,
    long points,
    LocalDateTime createdAt) {}
