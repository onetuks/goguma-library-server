package com.onetuks.libraryexternal.book.handler;

import com.onetuks.libraryexternal.book.handler.dto.IsbnResult;
import com.onetuks.libraryexternal.book.handler.strategy.BibliographyIsbnSearchStrategy;
import com.onetuks.libraryexternal.book.handler.strategy.CollectionIsbnSearchStrategy;
import com.onetuks.libraryexternal.book.handler.strategy.IsbnSearchStrategy;
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
        .map(strategy -> strategy.process(isbn))
        .filter(Objects::nonNull)
        .reduce(IsbnResult.init(), IsbnResult::update);
  }
}
