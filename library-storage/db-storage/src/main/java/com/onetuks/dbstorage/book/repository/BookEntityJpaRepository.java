package com.onetuks.dbstorage.book.repository;

import com.onetuks.dbstorage.book.entity.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookEntityJpaRepository extends JpaRepository<BookEntity, Long> {

  @Query(
      value =
          """
          SELECT * FROM books
                   WHERE JSON_OVERLAPS(books.categories, :interestedCategories)
                   ORDER BY RAND()
          """,
      nativeQuery = true)
  Page<BookEntity> findAllCategoriesInInterestedCategories(
      @Param("interestedCategories") String interestedCategories, Pageable pageable);
}
