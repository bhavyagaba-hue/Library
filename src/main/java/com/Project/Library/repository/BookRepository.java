package com.Project.Library.repository;

import com.Project.Library.entity.Book;
import com.Project.Library.enums.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findByStatus(BookStatus status);

    // Find the first available physical copy of a given stock title
    Optional<Book> findFirstByStockStockIdAndStatus(int stockId, BookStatus status);

    // All physical copies belonging to a stock entry
    List<Book> findByStockStockId(int stockId);
}