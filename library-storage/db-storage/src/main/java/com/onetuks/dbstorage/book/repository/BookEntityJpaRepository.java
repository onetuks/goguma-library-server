package com.onetuks.dbstorage.book.repository;

import com.onetuks.dbstorage.book.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookEntityJpaRepository extends JpaRepository<BookEntity, Long> {}
