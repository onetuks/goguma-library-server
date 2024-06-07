package com.onetuks.librarydomain.member.model.vo;

import com.onetuks.libraryobject.enums.ClientProvider;
import com.onetuks.libraryobject.enums.RoleType;
import java.util.List;
import lombok.Builder;

@Builder
public record AuthInfo(
    String socialId,
    ClientProvider clientProvider,
    List<RoleType> roles
) {}
