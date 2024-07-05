package com.onetuks.dbstorage.book.repository;

import com.onetuks.dbstorage.book.entity.BookEntity;
import com.onetuks.libraryobject.enums.Category;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookEntityJpaRepository extends JpaRepository<BookEntity, Long> {

  Page<BookEntity> findAllByCategoriesIn(List<Category> categories, Pageable pageable);
}
