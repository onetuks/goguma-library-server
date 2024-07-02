package com.onetuks.librarydomain.book.model;

import com.onetuks.libraryobject.enums.Category;
import com.onetuks.libraryobject.vo.ImageFile;
import java.util.List;

public record Book(
    Long bookId,
    String title,
    String authorName,
    String introduction,
    String isbn,
    String publisher,
    List<Category> categories,
    ImageFile coverImageFile,
    boolean isIndie,
    boolean isPermitted) {}
