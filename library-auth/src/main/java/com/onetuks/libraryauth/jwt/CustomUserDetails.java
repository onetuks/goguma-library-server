package com.onetuks.libraryauth.jwt;

import com.onetuks.libraryobject.annotation.Generated;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class CustomUserDetails implements UserDetails {

  private final String socialId;
  private final Long loginId;
  private final List<? extends GrantedAuthority> authorities;

  @Builder
  public CustomUserDetails(
      String socialId, Long loginId, List<? extends GrantedAuthority> authorities) {
    this.socialId = socialId;
    this.loginId = loginId;
    this.authorities = authorities;
  }

  @Override
  @Generated
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CustomUserDetails that = (CustomUserDetails) o;
    return Objects.equals(socialId, that.socialId)
        && Objects.equals(loginId, that.loginId)
        && Objects.equals(authorities, that.authorities);
  }

  @Generated
  @Override
  public int hashCode() {
    return Objects.hash(socialId, loginId, authorities);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", CustomUserDetails.class.getSimpleName() + "[", "]")
        .add("socialId='" + socialId + "'")
        .add("loginId=" + loginId)
        .add("authorities=" + authorities)
        .toString();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.authorities;
  }

  @Override
  public String getPassword() {
    return "";
  }

  @Override
  public String getUsername() {
    return this.socialId;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
