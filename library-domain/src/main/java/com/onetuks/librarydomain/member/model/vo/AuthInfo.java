package com.onetuks.librarydomain.member.model.vo;

import com.onetuks.libraryobject.enums.ClientProvider;
import com.onetuks.libraryobject.enums.RoleType;
import java.util.List;
import java.util.Objects;
import lombok.Builder;

@Builder
public record AuthInfo(String socialId, ClientProvider clientProvider, List<RoleType> roles) {

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AuthInfo authInfo = (AuthInfo) o;
    return Objects.equals(socialId, authInfo.socialId)
        && Objects.equals(roles, authInfo.roles)
        && clientProvider == authInfo.clientProvider;
  }

  @Override
  public int hashCode() {
    return Objects.hash(socialId, clientProvider, roles);
  }
}
