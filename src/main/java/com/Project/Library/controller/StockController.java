package com.Project.Library.controller;

import com.Project.Library.entity.Stock;
import com.Project.Library.service.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stock")
public class StockController {
    private final StockService stockService;

    public StockController(StockService stockService){
        this.stockService = stockService;
    }

    @GetMapping
    public List<Stock> getStock(){
        return stockService.getStock();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Stock addStock(@RequestBody Stock stock){
        return stockService.addStock(stock);
    }

    @GetMapping("/{id}")
    public Stock getStockById(@PathVariable int id){
        return stockService.getStockByID(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStock(@PathVariable int id) {
        stockService.deleteStock(id);
        return ResponseEntity.ok("Stock deleted successfully with id: " + id);
    }
}