package com.Project.Library.service;

import com.Project.Library.entity.Book;
import com.Project.Library.entity.Loan;
import com.Project.Library.entity.Stock;
import com.Project.Library.entity.User;
import com.Project.Library.enums.BookStatus;
import com.Project.Library.enums.LoanStatus;
import com.Project.Library.repository.BookRepository;
import com.Project.Library.repository.LoanRepository;
import com.Project.Library.repository.StockRepository;
import com.Project.Library.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final StockRepository stockRepository;

    public LoanService(LoanRepository loanRepository,
                       UserRepository userRepository,
                       BookRepository bookRepository,
                       StockRepository stockRepository) {
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.stockRepository = stockRepository;
    }

    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    public List<Loan> getLoansByUser(int userId) {
        return loanRepository.findByUserUserId(userId);
    }

    public List<Loan> getLoansByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return loanRepository.findByUserUserId(user.getUserId());
    }

    @Transactional
    public Loan issueLoan(int userId, int stockId) {

        // user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Stock stock = stockRepository.findByIdForUpdate(stockId)
                .orElseThrow(() -> new RuntimeException("Stock not found with id: " + stockId));

        // check availability
        if (stock.getAvailableCopies() <= 0) {
            throw new RuntimeException("Sorry, no copies of " + stock.getTitle() + " are currently available.");
        }

        // check duplicate active loan for the same title
        boolean alreadyBorrowed = loanRepository
                .checkActiveLoan(userId, stockId, LoanStatus.ACTIVE)
                .isPresent();
        if (alreadyBorrowed) {
            throw new RuntimeException("You already have an active loan for " + stock.getTitle() + ".");
        }

        // find available
        Book book = bookRepository.findFirstByStockStockIdAndStatus(stockId, BookStatus.AVAILABLE)
                .orElseThrow(() -> new RuntimeException("No available physical copy found for stock id: " + stockId));

        //borrow
        book.setStatus(BookStatus.BORROWED);
        bookRepository.save(book);

        stock.setAvailableCopies(stock.getAvailableCopies() - 1);
        stockRepository.save(stock);

        // loan record
        Loan loan = new Loan();
        loan.setUser(user);
        loan.setBook(book);
        loan.setIssueDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(14));
        loan.setStatus(LoanStatus.ACTIVE);

        return loanRepository.save(loan);
    }


    @Transactional
    public Loan returnLoan(int loanId) {

        // loan
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found with id: " + loanId));

        if (loan.getStatus() == LoanStatus.RETURNED) {
            throw new RuntimeException("Loan " + loanId + " has already been returned.");
        }

        // returned
        loan.setStatus(LoanStatus.RETURNED);
        loan.setReturnDate(LocalDate.now());

        //  physical copy as available
        Book book = loan.getBook();
        book.setStatus(BookStatus.AVAILABLE);
        bookRepository.save(book);

        // increment available count
        Stock stock = book.getStock();
        stock.setAvailableCopies(stock.getAvailableCopies() + 1);
        stockRepository.save(stock);

        return loanRepository.save(loan);
    }
}