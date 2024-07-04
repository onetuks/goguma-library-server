package com.onetuks.librarydomain.book.service.dto.param;

import com.onetuks.libraryobject.enums.Category;
import java.util.List;

public record BookPatchParam(
    String title,
    String authorName,
    String introduction,
    String isbn,
    String publisher,
    List<Category> categories,
    boolean isIndie,
    boolean isPermitted) {}
