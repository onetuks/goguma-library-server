package com.onetuks.libraryinjector;

import com.onetuks.librarydomain.book.handler.dto.IsbnResult;
import com.onetuks.librarydomain.book.service.BookService;
import com.onetuks.librarydomain.book.service.dto.param.BookPatchParam;
import com.onetuks.librarydomain.book.service.dto.param.BookPostParam;
import com.onetuks.libraryinjector.csv.CsvBookData;
import com.onetuks.libraryinjector.csv.CsvParser;
import com.onetuks.libraryobject.enums.Category;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DataInjectorService {

  private final CsvParser csvParser;
  private final BookService bookService;

  public DataInjectorService(CsvParser csvParser, BookService bookService) {
    this.csvParser = csvParser;
    this.bookService = bookService;
  }

  public void injectData(long adminId, MultipartFile csvFile) {
    List<CsvBookData> bookDatas = csvParser.parse(csvFile);

    bookDatas.parallelStream()
        .map(
            bookData -> {
              IsbnResult isbnResult = bookService.search(bookData.isbn());
              return new BookPostParam(
                  bookData.title(),
                  bookData.author(),
                  bookData.introduction(),
                  bookData.isbn(),
                  bookData.publisher(),
                  Category.parseToCategory(isbnResult.kdc()),
                  true,
                  isbnResult.coverImageUrl());
            })
        .map(param -> bookService.register(adminId, param, null))
        .forEach(
            book ->
                bookService.edit(
                    book.bookId(),
                    new BookPatchParam(
                        book.title(),
                        book.authorName(),
                        book.introduction(),
                        book.isbn(),
                        book.publisher(),
                        book.categories(),
                        book.isIndie(),
                        book.isPermitted(),
                        book.coverImageFile().fileName()),
                    null));
  }
}
