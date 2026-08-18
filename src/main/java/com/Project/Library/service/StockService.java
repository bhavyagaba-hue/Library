package com.Project.Library.service;

import com.Project.Library.entity.Stock;
import com.Project.Library.entity.User;
import com.Project.Library.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {
    private final StockRepository stockRepository;
    public StockService(StockRepository stockRepository){
        this.stockRepository = stockRepository;
    }

    public Stock addStock(Stock stock){
        return stockRepository.save(stock);
    }

    public List<Stock> getStock(){
        return stockRepository.findAll();
    }

    public Stock getStockByID(int id){
        return stockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock not found with id: " + id));
    }

    public void deleteStock(int id) {
        if (!stockRepository.existsById(id)) {
            throw new RuntimeException("Stock not found with id: " + id);
        }
        stockRepository.deleteById(id);
    }
}
