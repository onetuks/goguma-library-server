package com.onetuks.libraryapi.member.dto.request;

import com.onetuks.librarydomain.member.service.dto.param.MemberProfileParam;
import com.onetuks.libraryobject.enums.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;
import org.hibernate.validator.constraints.Length;

public record MemberPatchRequest(
    @NotBlank @Length(min = 2, max = 10) String nickname,
    @Length(max = 30) String introduction,
    String instagramUrl,
    @Size(min = 1, max = 3) Set<Category> interestedCategories,
    @NotNull boolean isAlarmAccepted) {

  public MemberProfileParam to() {
    return new MemberProfileParam(
        this.nickname(),
        this.introduction(),
        this.instagramUrl(),
        this.interestedCategories(),
        this.isAlarmAccepted());
  }
}
