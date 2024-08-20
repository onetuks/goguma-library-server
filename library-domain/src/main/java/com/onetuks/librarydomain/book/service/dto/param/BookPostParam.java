package com.onetuks.librarydomain.book.service.dto.param;

import com.onetuks.libraryobject.enums.Category;
import java.util.Set;

public record BookPostParam(
    String title,
    String authorName,
    String isbn,
    String publisher,
    Set<Category> categories,
    boolean isIndie,
    String coverImageFilename) {}
