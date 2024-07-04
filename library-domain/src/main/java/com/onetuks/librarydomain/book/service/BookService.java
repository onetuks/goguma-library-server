package com.onetuks.librarydomain.book.service;

import static com.onetuks.librarydomain.member.repository.PointRepository.BOOK_REGISTRATION_POINT;

import com.onetuks.librarydomain.book.model.Book;
import com.onetuks.librarydomain.book.repository.BookRepository;
import com.onetuks.librarydomain.book.service.dto.param.BookPatchParam;
import com.onetuks.librarydomain.book.service.dto.param.BookPostParam;
import com.onetuks.librarydomain.file.FileRepository;
import com.onetuks.librarydomain.member.repository.PointRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BookService {

  private final BookRepository bookRepository;
  private final FileRepository fileRepository;
  private final PointRepository pointRepository;

  public BookService(
      BookRepository bookRepository,
      FileRepository fileRepository,
      PointRepository pointRepository) {
    this.bookRepository = bookRepository;
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
  public Book edit(Long bookId, BookPatchParam param, MultipartFile coverImage) {
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
}
