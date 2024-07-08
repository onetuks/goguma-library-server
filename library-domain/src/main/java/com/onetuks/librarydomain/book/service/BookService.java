package com.onetuks.librarydomain.book.service;

import static com.onetuks.librarydomain.member.repository.PointRepository.BOOK_REGISTRATION_POINT;

import com.onetuks.librarydomain.book.model.Book;
import com.onetuks.librarydomain.book.repository.BookRepository;
import com.onetuks.librarydomain.book.service.dto.param.BookPatchParam;
import com.onetuks.librarydomain.book.service.dto.param.BookPostParam;
import com.onetuks.librarydomain.file.FileRepository;
import com.onetuks.librarydomain.member.repository.MemberRepository;
import com.onetuks.librarydomain.member.repository.PointRepository;
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
  private final PointRepository pointRepository;

  public BookService(
      BookRepository bookRepository,
      MemberRepository memberRepository,
      FileRepository fileRepository,
      PointRepository pointRepository) {
    this.bookRepository = bookRepository;
    this.memberRepository = memberRepository;
    this.fileRepository = fileRepository;
    this.pointRepository = pointRepository;
  }

  @Transactional
  public Book register(long loginId, BookPostParam param, MultipartFile coverImage) {
    Book book =
        Book.of(
            param.title(),
            param.authorName(),
            param.isbn(),
            param.publisher(),
            param.categories(),
            param.isIndie(),
            coverImage);

    pointRepository.creditPoints(loginId, BOOK_REGISTRATION_POINT);
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
                coverImage));
  }

  @Transactional
  public void remove(long bookId) {
    Book book = bookRepository.read(bookId);

    fileRepository.deleteFile(book.coverImageFile());
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

  @Transactional(readOnly = true)
  public Page<Book> searchWithKeyword(String keyword, Pageable pageable) {
    return bookRepository.readAll(keyword, pageable);
  }

  @Transactional(readOnly = true)
  public Page<Book> searchWithInterestedCategories(long loginId, Pageable pageable) {
    return bookRepository.readAll(memberRepository.read(loginId).interestedCategories(), pageable);
  }
}
