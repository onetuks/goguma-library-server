package com.onetuks.librarydomain.book.service;

import com.onetuks.librarydomain.book.handler.dto.IsbnResult;
import com.onetuks.librarydomain.book.model.Book;
import com.onetuks.librarydomain.book.repository.BookRepository;
import com.onetuks.librarydomain.book.service.dto.param.BookPatchParam;
import com.onetuks.librarydomain.book.service.dto.param.BookPostParam;
import com.onetuks.librarydomain.global.file.repository.FileRepository;
import com.onetuks.librarydomain.global.point.service.PointService;
import com.onetuks.librarydomain.member.repository.MemberRepository;
import com.onetuks.libraryobject.enums.CacheName;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BookService {

  private final BookRepository bookRepository;
  private final MemberRepository memberRepository;
  private final FileRepository fileRepository;

  private final PointService pointService;
  private final IsbnSearchService isbnSearchService;

  public BookService(
      BookRepository bookRepository,
      MemberRepository memberRepository,
      FileRepository fileRepository,
      PointService pointService,
      IsbnSearchService isbnSearchService) {
    this.bookRepository = bookRepository;
    this.memberRepository = memberRepository;
    this.fileRepository = fileRepository;
    this.pointService = pointService;
    this.isbnSearchService = isbnSearchService;
  }

  public IsbnResult search(String isbn) {
    return isbnSearchService.search(isbn);
  }

  @Transactional
  public Book register(long loginId, BookPostParam param, MultipartFile coverImage) {
    Book book =
        Book.of(
            memberRepository.read(loginId),
            param.title(),
            param.authorName(),
            param.introduction(),
            param.isbn(),
            param.publisher(),
            param.categories(),
            param.isIndie(),
            param.coverImageFilename(),
            coverImage);

    pointService.creditPointForBookRegistration(loginId);
    fileRepository.putFile(book.coverImageFile());

    return bookRepository.create(book);
  }

  @Transactional
  public Book edit(long bookId, BookPatchParam param, MultipartFile coverImage) {
    return bookRepository.update(
        bookRepository
            .read(bookId)
            .changeBookInfo(
                param.title(),
                param.authorName(),
                param.introduction(),
                param.isbn(),
                param.publisher(),
                param.categories(),
                param.isIndie(),
                param.isPermitted(),
                param.coverImageFilename(),
                coverImage));
  }

  @Transactional
  public void remove(long bookId) {
    Book book = bookRepository.read(bookId);

    fileRepository.deleteFile(book.coverImageFile());
    pointService.debitPointForBookRemoval(book.member().memberId());

    bookRepository.delete(bookId);
  }

  @Transactional(readOnly = true)
  public Page<Book> searchForInspection(boolean inspectionMode, Pageable pageable) {
    return bookRepository.readAll(inspectionMode, pageable);
  }

  @Transactional(readOnly = true)
  public Book search(long bookId) {
    return bookRepository.read(bookId);
  }

  @Cacheable(value = CacheName.SEARCHED_BOOKS, condition = "#keyword == null")
  @Transactional(readOnly = true)
  public Page<Book> searchWithKeyword(String keyword, Pageable pageable) {
    return bookRepository.readAll(keyword, pageable);
  }

  @Transactional(readOnly = true)
  public Page<Book> searchWithInterestedCategories(long loginId) {
    return bookRepository.readAll(memberRepository.read(loginId).interestedCategories());
  }
}
