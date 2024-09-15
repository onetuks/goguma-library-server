package com.onetuks.libraryobject.enums;

import lombok.Getter;

@Getter
public enum CacheType {
  WEEKLY_FEATURED_BOOKS(CacheName.WEEKLY_FEATURED_BOOKS, 60 * 60 * 24 * 7, 10),
  RECOMMENDED_MEMBERS(CacheName.RECOMMENDED_MEMBERS, 60 * 2, 100),
  REVIEW_FEED(CacheName.REVIEW_FEED, 60, 1_000),
  BOOK_PICKS(CacheName.BOOK_PICKS, 60, 1_000),
  REVIEW_PICKS(CacheName.REVIEW_PICKS, 60, 1_000),
  MEMBER_FOLLOWS(CacheName.MEMBER_FOLLOWS, 60, 1_000),
  SEARCHED_BOOKS(CacheName.SEARCHED_BOOKS, 60, 1_000);

  private final String cacheName;
  private final long expirationAfterWrite;
  private final long maximumCacheSize;

  CacheType(String cacheName, long expirationAfterWrite, long maximumCacheSize) {
    this.cacheName = cacheName;
    this.expirationAfterWrite = expirationAfterWrite;
    this.maximumCacheSize = maximumCacheSize;
  }
}
