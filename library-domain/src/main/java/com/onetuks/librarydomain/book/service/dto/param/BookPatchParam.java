package com.onetuks.librarydomain.book.service.dto.param;

import com.onetuks.libraryobject.enums.Category;
import java.util.Set;

public record BookPatchParam(
    String title,
    String authorName,
    String introduction,
    String isbn,
    String publisher,
    Set<Category> categories,
    boolean isIndie,
    boolean isPermitted,
    String coverImageFilename) {}
