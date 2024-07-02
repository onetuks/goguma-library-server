package com.onetuks.libraryobject.enums;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum Category {
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

  private static final Map<String, List<Category>> KDC_TO_CATEGORY_MAP = new ConcurrentHashMap<>();

  static {
    KDC_TO_CATEGORY_MAP.put("0xx", List.of(NON_LITERATURE));
    KDC_TO_CATEGORY_MAP.put("1xx", List.of(NON_LITERATURE));
    KDC_TO_CATEGORY_MAP.put("2xx", List.of(NON_LITERATURE));
    KDC_TO_CATEGORY_MAP.put("3xx", List.of(NON_LITERATURE));
    KDC_TO_CATEGORY_MAP.put("4xx", List.of(NON_LITERATURE));
    KDC_TO_CATEGORY_MAP.put("5xx", List.of(NON_LITERATURE));
    KDC_TO_CATEGORY_MAP.put("7xx", List.of(NON_LITERATURE));
    KDC_TO_CATEGORY_MAP.put("9xx", List.of(NON_LITERATURE));
    KDC_TO_CATEGORY_MAP.put("657", List.of(CARTOON));
    KDC_TO_CATEGORY_MAP.put("65x", List.of(ART_BOOK));
    KDC_TO_CATEGORY_MAP.put("66x", List.of(PHOTO));
    KDC_TO_CATEGORY_MAP.put("6xx", List.of(ESSAY));
    KDC_TO_CATEGORY_MAP.put("804", List.of(ESSAY));
    KDC_TO_CATEGORY_MAP.put("805", List.of(MAGAZINE));
    KDC_TO_CATEGORY_MAP.put("8x1", List.of(POETRY));
    KDC_TO_CATEGORY_MAP.put("8x2", List.of(NOVEL));
    KDC_TO_CATEGORY_MAP.put("8x3", List.of(NOVEL));
    KDC_TO_CATEGORY_MAP.put("8x4", List.of(ESSAY));
    KDC_TO_CATEGORY_MAP.put("8x5", List.of(ESSAY));
    KDC_TO_CATEGORY_MAP.put("8x6", List.of(TRAVEL, ESSAY));
    KDC_TO_CATEGORY_MAP.put("8x7", List.of(ESSAY));
    KDC_TO_CATEGORY_MAP.put("8x8", List.of(PHOTO, ESSAY));
  }

  public static List<Category> parseRemainCode(String kdc) {
    if (kdc == null || kdc.isEmpty()) {
      return List.of(ETC);
    }

    String regulatedKdc = kdc + "x".repeat(3 - kdc.length());
    return regulatedKdc.length() < 3
        ? List.of(ETC)
        : KDC_TO_CATEGORY_MAP.getOrDefault(getSimilarKey(regulatedKdc), List.of(NON_LITERATURE));
  }

  private static String getSimilarKey(String kdc) {
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
