package com.onetuks.libraryexternal.book.handler.strategy;

import com.onetuks.libraryexternal.book.handler.dto.IsbnResult;
import org.springframework.util.MultiValueMap;

public interface IsbnSearchStrategy {

  IsbnResult process(String isbn);

  MultiValueMap<String, String> buildMultiValueMap(String isbn);
}
