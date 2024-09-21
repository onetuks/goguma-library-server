package com.onetuks.libraryinjector.csv;

import java.util.Objects;

public record CsvBookData(
    String number,
    String title,
    String author,
    String publisher,
    String isbn,
    String introduction) {

  private static final String DASH = "-";

  public CsvBookData(
      String number,
      String title,
      String author,
      String publisher,
      String isbn,
      String introduction) {
    this.number = Objects.equals(number, DASH) ? null : number;
    this.title = Objects.equals(title, DASH) ? null : title;
    this.author = Objects.equals(author, DASH) ? null : author;
    this.publisher = Objects.equals(publisher, DASH) ? null : publisher;
    this.isbn = Objects.equals(isbn, DASH) ? null : isbn;
    this.introduction = Objects.equals(introduction, DASH) ? null : introduction;
  }
}
