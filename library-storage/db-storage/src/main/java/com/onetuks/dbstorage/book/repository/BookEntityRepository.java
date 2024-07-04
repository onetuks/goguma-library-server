package com.onetuks.dbstorage.book.repository;

import com.onetuks.dbstorage.book.converter.BookConverter;
import com.onetuks.librarydomain.book.model.Book;
import com.onetuks.librarydomain.book.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class BookEntityRepository implements BookRepository {

  private final BookEntityJpaRepository repository;
  private final BookEntityJpaQueryDslRepository qDslRepository;
  private final BookConverter converter;

  public BookEntityRepository(
      BookEntityJpaRepository repository,
      BookEntityJpaQueryDslRepository qDslRepository,
      BookConverter converter) {
    this.repository = repository;
    this.qDslRepository = qDslRepository;
    this.converter = converter;
  }

  @Override
  public Book create(Book book) {
    return converter.toDomain(repository.save(converter.toEntity(book)));
  }

  @Override
  public Book read(long bookId) {
    return converter.toDomain(
        repository
            .findById(bookId)
            .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 도서입니다.")));
  }

  @Override
  public Page<Book> readAll(boolean inspectionMode, Pageable pageable) {
    return qDslRepository.findAllByIsPermitted(inspectionMode, pageable).map(converter::toDomain);
  }

  @Override
  public Book update(Book book) {
    return converter.toDomain(repository.save(converter.toEntity(book)));
  }

  @Override
  public void delete(long bookId) {
    repository.deleteById(bookId);
  }
}
