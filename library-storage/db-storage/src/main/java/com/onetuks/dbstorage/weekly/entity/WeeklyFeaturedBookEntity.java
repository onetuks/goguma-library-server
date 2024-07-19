package com.onetuks.dbstorage.weekly.entity;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;

import com.onetuks.dbstorage.book.entity.BookEntity;
import com.onetuks.libraryobject.annotation.Generated;
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
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    name = "weekly_featured_books",
    uniqueConstraints =
        @UniqueConstraint(
            name = "unq_book_id",
            columnNames = {"book_id"}))
public class WeeklyFeaturedBookEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "weekly_featured_book_id", nullable = false)
  private Long weeklyFeaturedBookId;

  @ManyToOne(
      fetch = FetchType.LAZY,
      cascade = {PERSIST, REMOVE})
  @JoinColumn(name = "weekly_featured_books_event_id", nullable = false)
  private WeeklyFeaturedBooksEventEntity weeklyFeaturedBooksEventEntity;

  @ManyToOne(
      fetch = FetchType.LAZY,
      cascade = {REMOVE})
  @JoinColumn(name = "book_id", nullable = false)
  private BookEntity book;

  public WeeklyFeaturedBookEntity(
      Long weeklyFeaturedBookId,
      WeeklyFeaturedBooksEventEntity weeklyFeaturedBooksEventEntity,
      BookEntity book) {
    this.weeklyFeaturedBookId = weeklyFeaturedBookId;
    this.weeklyFeaturedBooksEventEntity = weeklyFeaturedBooksEventEntity;
    this.book = book;
  }

  @Override
  @Generated
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WeeklyFeaturedBookEntity that = (WeeklyFeaturedBookEntity) o;
    return Objects.equals(weeklyFeaturedBookId, that.weeklyFeaturedBookId);
  }

  @Override
  @Generated
  public int hashCode() {
    return Objects.hashCode(weeklyFeaturedBookId);
  }
}
