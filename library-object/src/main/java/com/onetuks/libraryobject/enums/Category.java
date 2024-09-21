package com.onetuks.libraryobject.enums;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public enum Category {
  ALL, // 전체
  ETC, // 기타
  POETRY, // 시
  ESSAY, // 에세이
  TRAVEL, // 여행
  PHOTO, // 사진
  ILLUSTRATION, // 일러스트
  CARTOON, // 만화
  MAGAZINE, // 잡지
  NOVEL, // 소설
  ART_BOOK, // 미술
  NON_LITERATURE; // 비문학

  private static final Map<String, Set<Category>> KDC_TO_CATEGORY_MAP = new ConcurrentHashMap<>();

  static {
    KDC_TO_CATEGORY_MAP.put("0xx", Set.of(NON_LITERATURE));
    KDC_TO_CATEGORY_MAP.put("1xx", Set.of(NON_LITERATURE));
    KDC_TO_CATEGORY_MAP.put("2xx", Set.of(NON_LITERATURE));
    KDC_TO_CATEGORY_MAP.put("3xx", Set.of(NON_LITERATURE));
    KDC_TO_CATEGORY_MAP.put("4xx", Set.of(NON_LITERATURE));
    KDC_TO_CATEGORY_MAP.put("5xx", Set.of(NON_LITERATURE));
    KDC_TO_CATEGORY_MAP.put("7xx", Set.of(NON_LITERATURE));
    KDC_TO_CATEGORY_MAP.put("9xx", Set.of(NON_LITERATURE));
    KDC_TO_CATEGORY_MAP.put("657", Set.of(CARTOON));
    KDC_TO_CATEGORY_MAP.put("65x", Set.of(ART_BOOK));
    KDC_TO_CATEGORY_MAP.put("66x", Set.of(PHOTO));
    KDC_TO_CATEGORY_MAP.put("6xx", Set.of(ESSAY));
    KDC_TO_CATEGORY_MAP.put("804", Set.of(ESSAY));
    KDC_TO_CATEGORY_MAP.put("805", Set.of(MAGAZINE));
    KDC_TO_CATEGORY_MAP.put("8x1", Set.of(POETRY));
    KDC_TO_CATEGORY_MAP.put("8x2", Set.of(NOVEL));
    KDC_TO_CATEGORY_MAP.put("8x3", Set.of(NOVEL));
    KDC_TO_CATEGORY_MAP.put("8x4", Set.of(ESSAY));
    KDC_TO_CATEGORY_MAP.put("8x5", Set.of(ESSAY));
    KDC_TO_CATEGORY_MAP.put("8x6", Set.of(TRAVEL, ESSAY));
    KDC_TO_CATEGORY_MAP.put("8x7", Set.of(ESSAY));
    KDC_TO_CATEGORY_MAP.put("8x8", Set.of(PHOTO, ESSAY));
  }

  public static Set<Category> parseToCategory(String kdc) {
    if (kdc == null || kdc.isEmpty()) {
      return Set.of(ETC);
    }

    String regulatedKdc = kdc + "x".repeat(3 - kdc.length());
    return regulatedKdc.length() < 3
        ? Set.of(ETC)
        : KDC_TO_CATEGORY_MAP.getOrDefault(getSimilarCode(regulatedKdc), Set.of(NON_LITERATURE));
  }

  private static String getSimilarCode(String kdc) {
    String similarKey = null;
    int maxScore = 0;

    for (String key : KDC_TO_CATEGORY_MAP.keySet()) {
      int score = 0;
      for (int i = 0; i < key.length(); i++) {
        char patternToken = key.charAt(i);
        char targetToken = kdc.charAt(i);

        if (patternToken == 'x') {
          score += 1;
        } else if (patternToken == targetToken) {
          score += 2;
        }
      }

      if (maxScore < score) {
        maxScore = score;
        similarKey = key;
      }
    }

    return similarKey;
  }
}
