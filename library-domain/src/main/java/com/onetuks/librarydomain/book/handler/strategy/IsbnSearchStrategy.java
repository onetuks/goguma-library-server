package com.onetuks.librarydomain.book.handler.strategy;

import com.onetuks.librarydomain.book.handler.dto.IsbnResult;
import org.springframework.util.MultiValueMap;

public interface IsbnSearchStrategy {

  IsbnResult process(String isbn);

  MultiValueMap<String, String> buildMultiValueMap(String isbn);
}
