package com.onetuks.librarydomain.book.handler;

import com.onetuks.librarydomain.book.handler.dto.IsbnResult;
import com.onetuks.librarydomain.book.handler.strategy.BibliographyIsbnSearchStrategy;
import com.onetuks.librarydomain.book.handler.strategy.CollectionIsbnSearchStrategy;
import com.onetuks.librarydomain.book.handler.strategy.IsbnSearchStrategy;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class IsbnSearchHandler {

  private final List<IsbnSearchStrategy> strategies;

  public IsbnSearchHandler(
      BibliographyIsbnSearchStrategy bibliographyIsbnSearchStrategy,
      CollectionIsbnSearchStrategy collectionIsbnSearchStrategy) {
    this.strategies = List.of(bibliographyIsbnSearchStrategy, collectionIsbnSearchStrategy);
  }

  public IsbnResult handle(String isbn) {
    return strategies.stream()
        .map(strategy -> {
          try {
            return strategy.process(isbn);
          } catch (IllegalStateException e) {
            return null;
          }
        })
        .filter(Objects::nonNull)
        .reduce(IsbnResult.init(), IsbnResult::update);
  }
}
