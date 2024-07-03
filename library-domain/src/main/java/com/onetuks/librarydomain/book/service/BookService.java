package com.onetuks.librarydomain.book.service;

import com.onetuks.librarydomain.book.model.Book;
import com.onetuks.librarydomain.book.repository.BookRepository;
import com.onetuks.librarydomain.book.service.dto.param.BookPostParam;
import com.onetuks.librarydomain.file.FileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BookService {

  private final BookRepository bookRepository;
  private final FileRepository fileRepository;

  public BookService(BookRepository bookRepository, FileRepository fileRepository) {
    this.bookRepository = bookRepository;
    this.fileRepository = fileRepository;
  }

  @Transactional
  public Book register(BookPostParam param, MultipartFile coverImage) {
    Book book =
        Book.of(
            param.title(),
            param.authorName(),
            param.isbn(),
            param.publisher(),
            param.categories(),
            param.isIndie(),
            coverImage);

    fileRepository.putFile(book.coverImageFile());

    return bookRepository.create(book);
  }
}
