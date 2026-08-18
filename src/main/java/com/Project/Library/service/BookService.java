package com.Project.Library.service;

import com.Project.Library.entity.Book;
import com.Project.Library.entity.Stock;
import com.Project.Library.enums.BookStatus;
import com.Project.Library.repository.BookRepository;
import com.Project.Library.repository.StockRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final StockRepository stockRepository;

    public BookService(BookRepository bookRepository, StockRepository stockRepository) {
        this.bookRepository = bookRepository;
        this.stockRepository = stockRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookByID(int id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
    }

    @Transactional
    public Book addBook(Book book) {
        Stock stock = stockRepository.findById(book.getStock().getStockId())
                .orElseThrow(() -> new RuntimeException("Stock not found with id: " + book.getStock().getStockId()));

        book.setStock(stock);
        book.setStatus(BookStatus.AVAILABLE);
        Book saved = bookRepository.save(book);

        stock.setTotalCopies(stock.getTotalCopies() + 1);
        stock.setAvailableCopies(stock.getAvailableCopies() + 1);
        stockRepository.save(stock);

        return saved;
    }


    @Transactional
    public void deleteBook(int id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));

        Stock stock = book.getStock();

        stock.setTotalCopies(stock.getTotalCopies() - 1);

        if (book.getStatus() == BookStatus.AVAILABLE) {
            stock.setAvailableCopies(stock.getAvailableCopies() - 1);
        }

        stockRepository.save(stock);
        bookRepository.deleteById(id);
    }
}