package com.onetuks.dbstorage.weakly;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;

import com.onetuks.dbstorage.book.entity.BookEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    name = "weakly_featured_books",
    uniqueConstraints = @UniqueConstraint(
        name = "unq_weakly_featured_book_event_id_book_id",
        columnNames = {"weakly_featured_book_event_id", "book_id"}))
public class WeaklyFeaturedBookEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "weakly_featured_book_id", nullable = false)
  private Long weaklyFeaturedBookId;

  @ManyToOne(fetch = FetchType.LAZY, cascade = {PERSIST, REMOVE})
  @JoinColumn(name = "weakly_featured_books_event_id", nullable = false)
  private WeaklyFeaturedBooksEventEntity weaklyFeaturedBooksEventEntity;

  @ManyToOne(fetch = FetchType.LAZY, cascade = {PERSIST, REMOVE})
  @JoinColumn(name = "book_id", nullable = false)
  private BookEntity book;

  public WeaklyFeaturedBookEntity(
      Long weaklyFeaturedBookId,
      WeaklyFeaturedBooksEventEntity weaklyFeaturedBooksEventEntity,
      BookEntity book) {
    this.weaklyFeaturedBookId = weaklyFeaturedBookId;
    this.weaklyFeaturedBooksEventEntity = weaklyFeaturedBooksEventEntity;
    this.book = book;
  }
}
