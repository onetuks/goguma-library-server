package com.onetuks.librarydomain.weakly;

import com.onetuks.librarydomain.book.model.Book;

public record WeaklyFeaturedBook(
    Long weaklyFeaturedBookId,
    WeaklyFeaturedBooksEvent weaklyFeaturedBooksEvent,
    Book book) {}
