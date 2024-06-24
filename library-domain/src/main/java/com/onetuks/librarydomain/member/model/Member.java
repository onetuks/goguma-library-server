package com.onetuks.librarydomain.member.model;

import com.onetuks.librarydomain.member.model.vo.AuthInfo;
import com.onetuks.librarydomain.member.model.vo.Nickname;
import com.onetuks.libraryobject.enums.Category;
import com.onetuks.libraryobject.vo.ImageFile;
import java.util.List;
import lombok.Builder;

@Builder
public record Member(
    Long memberId,
    AuthInfo authInfo,
    Nickname nickname,
    String introduction,
    List<Category> interestedCategories,
    long points,
    boolean isAlarmAccepted,
    ImageFile profileImageFile,
    MemberStatics memberStatics) {}
