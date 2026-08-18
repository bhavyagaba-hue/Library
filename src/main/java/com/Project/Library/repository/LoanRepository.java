package com.Project.Library.repository;

import com.Project.Library.entity.Loan;
import com.Project.Library.enums.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Integer> {
    List<Loan> findByUserUserId(int userId);
    List<Loan> findByStatus(LoanStatus status);

    // Check for duplicate active loan: same user + same stock title
    @Query("SELECT l FROM Loan l WHERE l.user.userId = :userId AND l.book.stock.stockId = :stockId AND l.status = :status")
    Optional<Loan> checkActiveLoan(
            @Param("userId") int userId,
            @Param("stockId") int stockId,
            @Param("status") LoanStatus status);
}
