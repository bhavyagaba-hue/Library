package com.Project.Library.controller;

import com.Project.Library.entity.Loan;
import com.Project.Library.service.LoanService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Loan> getAllLoans() {
        return loanService.getAllLoans();
    }

    @GetMapping("/my")
    public List<Loan> getMyLoans(Authentication authentication) {
        return loanService.getLoansByEmail(authentication.getName());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{id}")
    public List<Loan> getUserLoans(@PathVariable int id) {
        return loanService.getLoansByUser(id);
    }


    @PostMapping("/borrow/{userId}/{stockId}")
    public ResponseEntity<Loan> borrowBook(@PathVariable int userId,
                                           @PathVariable int stockId) {
        Loan loan = loanService.issueLoan(userId, stockId);
        return ResponseEntity.ok(loan);
    }

    @PutMapping("/return/{id}")
    public ResponseEntity<Loan> returnBook(@PathVariable int id) {
        Loan loan = loanService.returnLoan(id);
        return ResponseEntity.ok(loan);
    }
}