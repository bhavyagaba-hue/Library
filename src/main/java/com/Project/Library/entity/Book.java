package com.Project.Library.entity;

import com.Project.Library.enums.BookStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookId;

    @ManyToOne
    @JoinColumn(name = "stock_id")
    private Stock stock;

    @Enumerated(EnumType.STRING)
    private BookStatus status;
}