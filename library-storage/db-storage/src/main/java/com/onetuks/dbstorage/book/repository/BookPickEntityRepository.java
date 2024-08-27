package com.onetuks.dbstorage.book.repository;

import com.onetuks.dbstorage.book.converter.BookPickConverter;
import com.onetuks.librarydomain.book.model.BookPick;
import com.onetuks.librarydomain.book.repository.BookPickRepository;
import com.onetuks.libraryobject.exception.NoSuchEntityException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class BookPickEntityRepository implements BookPickRepository {

  private final BookPickEntityJpaRepository repository;
  private final BookPickConverter converter;

  public BookPickEntityRepository(
      BookPickEntityJpaRepository repository, BookPickConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }

  @Override
  public BookPick create(BookPick bookPick) {
    return converter.toModel(repository.save(converter.toEntity(bookPick)));
  }

  @Override
  public BookPick read(long bookPickId) {
    return converter.toModel(
        repository
            .findById(bookPickId)
            .orElseThrow(() -> new NoSuchEntityException("존재하지 않는 북픽입니다.")));
  }

  @Override
  public BookPick read(long memberId, long bookId) {
    return converter.toModel(
        repository
            .findByMemberEntityMemberIdAndBookEntityBookId(memberId, bookId)
            .orElseThrow(() -> new NoSuchEntityException("존재하지 않는 북픽입니다.")));
  }

  @Override
  public Page<BookPick> readAll(long memberId, Pageable pageable) {
    return repository.findAllByMemberEntityMemberId(memberId, pageable).map(converter::toModel);
  }

  @Override
  public void delete(long bookPickId) {
    repository.deleteById(bookPickId);
  }
}
