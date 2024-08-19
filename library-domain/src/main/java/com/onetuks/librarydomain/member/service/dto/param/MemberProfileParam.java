package com.onetuks.librarydomain.member.service.dto.param;

import com.onetuks.libraryobject.enums.Category;
import java.util.Set;

public record MemberProfileParam(
    String nickname,
    String introduction,
    String instagramUrl,
    Set<Category> interestedCategories,
    boolean isAlarmAccepted) {}
