package rs.lab.rpcdemo.stock.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.lab.rpcdemo.stock.dto.StockDto;
import rs.lab.rpcdemo.stock.dto.mapper.StockMapper;
import rs.lab.rpcdemo.stock.services.StockService;

import java.util.Collection;

@RestController
public class StockController {

    @Autowired
    private StockService stockService;

    @Autowired
    private StockMapper mapper;

    @GetMapping("/stock")
    public ResponseEntity<Collection<StockDto>> getStocks(@PageableDefault Pageable pageable) {
        var stocks = stockService.getStocks(pageable);
        return ResponseEntity.ok(mapper.toStocksDto(stocks));
    }

    @GetMapping("/stock/{name}")
    public ResponseEntity<StockDto> getStocks(@RequestParam String name) {
        var stock = stockService.getStock(name);
        if(stock.isPresent()) {
            ResponseEntity.ok(mapper.toStockDto(stock.get()));
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/stock")
    public ResponseEntity<StockDto> addStock(@RequestBody StockDto stock) {
        var stockEntity = stockService.addStock(stock);
        return ResponseEntity.ok(mapper.toStockDto(stockEntity));
    }
}
