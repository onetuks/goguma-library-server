package com.onetuks.librarydomain.book.service;

import com.onetuks.librarydomain.book.repository.BookRepository;
import com.onetuks.librarydomain.file.FileRepository;
import org.springframework.stereotype.Service;

@Service
public class BookService {

  private final BookRepository bookRepository;
  private final FileRepository fileRepository;

  public BookService(BookRepository bookRepository, FileRepository fileRepository) {
    this.bookRepository = bookRepository;
    this.fileRepository = fileRepository;
  }
}
