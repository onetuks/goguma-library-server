package com.onetuks.libraryauth.jwt.repository;

import com.onetuks.libraryauth.jwt.repository.entity.AuthTokenPair;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface AuthTokenRepository extends CrudRepository<AuthTokenPair, String> {

  default void save(String accessToken, String refreshToken) {
    this.save(new AuthTokenPair(accessToken, refreshToken));
  }

  default void delete(String accessToken) {
    this.deleteById(accessToken);
  }

  default Optional<String> findRefreshToken(String accessToken) {
    return this.findById(accessToken).map(AuthTokenPair::getRefreshToken);
  }
}
