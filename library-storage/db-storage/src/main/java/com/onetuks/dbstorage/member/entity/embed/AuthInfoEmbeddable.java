package com.onetuks.dbstorage.member.entity.embed;

import com.onetuks.libraryobject.enums.ClientProvider;
import com.onetuks.libraryobject.enums.RoleType;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class AuthInfoEmbeddable {

  @Column(name = "social_id", nullable = false)
  private String socialId;

  @Column(name = "client_provider", nullable = false)
  private ClientProvider clientProvider;

  @Type(JsonType.class)
  @Column(name = "roles", nullable = false)
  private List<RoleType> roles;

  public AuthInfoEmbeddable(String socialId, ClientProvider clientProvider, List<RoleType> roles) {
    this.socialId = socialId;
    this.clientProvider = clientProvider;
    this.roles = roles;
  }
}
