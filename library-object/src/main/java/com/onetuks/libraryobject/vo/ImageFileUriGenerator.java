package com.onetuks.libraryobject.vo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.springframework.stereotype.Component;

@Component
public class ImageFileUriGenerator {

  private static final String HASH_ALGORITHM = "SHA-256";

  public static String generateUri(long id) {
    try {
      MessageDigest messageDigest = MessageDigest.getInstance(HASH_ALGORITHM);
      messageDigest.update(String.valueOf(id).getBytes());
      byte[] digest = messageDigest.digest();
      StringBuilder stringBuilder = new StringBuilder();
      for (byte b : digest) {
        stringBuilder.append(String.format("%02x", b));
      }
      return stringBuilder.toString();
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }
}
