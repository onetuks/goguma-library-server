package com.onetuks.libraryexternal.book.service;

import com.onetuks.libraryexternal.book.handler.IsbnSearchHandler;
import com.onetuks.libraryexternal.book.handler.dto.IsbnResult;
import org.springframework.stereotype.Service;

@Service
public class IsbnSearchService {

  private final IsbnSearchHandler handler;

  public IsbnSearchService(IsbnSearchHandler handler) {
    this.handler = handler;
  }

  public IsbnResult search(String isbn) {
    return handler.handle(isbn);
  }
}
