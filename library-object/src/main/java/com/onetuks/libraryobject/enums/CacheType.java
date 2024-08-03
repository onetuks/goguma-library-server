package com.onetuks.libraryobject.enums;

import lombok.Getter;

@Getter
public enum CacheType {
  WEEKLY_FEATURED_BOOKS("weekly_featured_books", 60 * 60 * 24 * 7, 10),
  RECOMMENDED_MEMBERS("recommended_members", 60 * 60 * 24, 100),
  REVIEW_FEED("review_feed", 60 * 20, 1_000),
  BOOK_PICKS("book_picks", 60 * 20, 1_000),
  REVIEW_PICKS("review_picks", 60 * 20, 1_000),
  MEMBER_FOLLOWS("member_follows", 60 * 20, 1_000);

  private final String cacheName;
  private final long expirationAfterWrite;
  private final long maximumCacheSize;

  CacheType(String cacheName, long expirationAfterWrite, long maximumCacheSize) {
    this.cacheName = cacheName;
    this.expirationAfterWrite = expirationAfterWrite;
    this.maximumCacheSize = maximumCacheSize;
  }
}
