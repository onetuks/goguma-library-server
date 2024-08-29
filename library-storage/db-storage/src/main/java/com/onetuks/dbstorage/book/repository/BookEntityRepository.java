package com.onetuks.dbstorage.book.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetuks.dbstorage.book.converter.BookConverter;
import com.onetuks.dbstorage.book.entity.BookEntity;
import com.onetuks.librarydomain.book.model.Book;
import com.onetuks.librarydomain.book.repository.BookRepository;
import com.onetuks.libraryobject.enums.Category;
import com.onetuks.libraryobject.exception.NoSuchEntityException;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class BookEntityRepository implements BookRepository {

  private static final ObjectMapper objectMapper = new ObjectMapper();

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
    return converter.toModel(repository.save(converter.toEntity(book)));
  }

  @Override
  public Book read(long bookId) {
    return converter.toModel(
        repository
            .findById(bookId)
            .orElseThrow(() -> new NoSuchEntityException("존재하지 않는 도서입니다.")));
  }

  @Override
  public Page<Book> readAll(boolean inspectionMode, Pageable pageable) {
    return qDslRepository.findAllByIsPermitted(inspectionMode, pageable).map(converter::toModel);
  }

  @Override
  public Page<Book> readAll(String keyword, Pageable pageable) {
    return qDslRepository.findAllByKeyword(keyword, pageable).map(converter::toModel);
  }

  @Override
  public Page<Book> readAll(Set<Category> interestedCategories) {
    try {
      Pageable pageable = PageRequest.of(0, INTERESTED_CATEGORIES_RECOMMEND_BOOKS_COUNT);

      Page<BookEntity> results =
          interestedCategories.contains(Category.ALL)
              ? repository.findAll(pageable)
              : repository.findAllCategoriesInInterestedCategories(
                  objectMapper.writeValueAsString(interestedCategories), pageable);

      return results.map(converter::toModel);
    } catch (JsonProcessingException e) {
      throw new IllegalStateException("카테고리 조회 중 오류가 발생했습니다.");
    }
  }

  @Override
  public List<Book> readAllNotIn(List<Book> allWeeklyFeaturedBooks) {
    return repository
        .findAllNotInPastWeeklyFeaturedBooks(
            allWeeklyFeaturedBooks.stream().map(Book::bookId).toList(),
            PageRequest.of(0, WEEKLY_FEATURED_RECOMMEND_BOOKS_COUNT))
        .getContent()
        .stream()
        .map(converter::toModel)
        .toList();
  }

  @Override
  public Book update(Book book) {
    return converter.toModel(repository.save(converter.toEntity(book)));
  }

  @Override
  public void delete(long bookId) {
    repository.deleteById(bookId);
  }
}
