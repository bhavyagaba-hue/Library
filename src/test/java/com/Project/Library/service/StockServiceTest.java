package com.Project.Library.service;

import com.Project.Library.entity.Stock;
import com.Project.Library.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private StockService stockService;

    private Stock stock;

    @BeforeEach
    void setUp() {
        stock = new Stock();
        stock.setStockId(1);
        stock.setTitle("Harry Potter");
        stock.setAuthor("JK rowling");
        stock.setTotalCopies(5);
        stock.setAvailableCopies(5);
    }

    @Test
    void addStock_savesAndReturnsStock() {
        when(stockRepository.save(stock)).thenReturn(stock);

        Stock result = stockService.addStock(stock);

        assertEquals("Harry Potter", result.getTitle());
        verify(stockRepository, times(1)).save(stock);
    }

    @Test
    void getStock_returnsAllStock() {
        when(stockRepository.findAll()).thenReturn(List.of(stock));

        List<Stock> result = stockService.getStock();

        assertEquals(1, result.size());
        assertEquals("Harry Potter", result.get(0).getTitle());
    }

    @Test
    void getStockByID_returnsStock() {
        when(stockRepository.findById(1)).thenReturn(Optional.of(stock));

        Stock result = stockService.getStockByID(1);

        assertEquals(1, result.getStockId());
        assertEquals("Harry Potter", result.getTitle());
    }

    @Test
    void getStockByID_throwsExceptionWhenNotFound() {
        when(stockRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> stockService.getStockByID(99));
    }

    @Test
    void deleteStock_deletesWhenExists() {
        when(stockRepository.existsById(1)).thenReturn(true);

        stockService.deleteStock(1);

        verify(stockRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteStock_throwsException_whenNotFound() {
        when(stockRepository.existsById(99)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> stockService.deleteStock(99));
    }
}