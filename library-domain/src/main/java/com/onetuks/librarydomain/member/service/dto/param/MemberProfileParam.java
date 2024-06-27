package com.onetuks.librarydomain.member.service.dto.param;

import com.onetuks.libraryobject.enums.Category;
import java.util.List;

public record MemberProfileParam(
    String nickname,
    String introduction,
    List<Category> interestedCategories,
    boolean isAlarmAccepted) {}
