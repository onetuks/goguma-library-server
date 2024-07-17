package com.onetuks.libraryauth.oauth.strategy.dto.user_info;

import com.onetuks.librarydomain.member.model.vo.AuthInfo;
import com.onetuks.libraryobject.enums.ClientProvider;
import com.onetuks.libraryobject.enums.RoleType;
import java.util.Set;
import lombok.Builder;

@Builder
public record UserInfo(String socialId, ClientProvider clientProvider, Set<RoleType> roles) {

  public AuthInfo toDomain() {
    return new AuthInfo(socialId, clientProvider, roles);
  }
}
