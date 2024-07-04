package com.onetuks.libraryapi.member.dto.request;

import com.onetuks.librarydomain.member.service.dto.param.MemberProfileParam;
import com.onetuks.libraryobject.enums.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.hibernate.validator.constraints.Length;

public record MemberPatchRequest(
    @NotBlank @Length(min = 2, max = 10) String nickname,
    @Length(max = 50) String introduction,
    @Size(min = 1, max = 3) List<Category> interestedCategories,
    @NotNull boolean isAlarmAccepted) {

  public MemberProfileParam to() {
    return new MemberProfileParam(
        this.nickname(), this.introduction(), this.interestedCategories(), this.isAlarmAccepted());
  }
}
