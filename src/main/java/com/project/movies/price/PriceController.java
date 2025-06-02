package com.project.movies.price;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4000")
@RestController
@RequestMapping("/api/prices")
public class PriceController {

    @Autowired
    private PriceService priceService;

    @GetMapping
    public ResponseEntity<List<PriceModel>> getAllPrices() {
        return priceService.getAllPrices();
    }

    @PostMapping
    public ResponseEntity<PriceModel> createPrice(@RequestBody PriceModel price) {
        return priceService.createPrice(price);
    }

    @PutMapping
    public ResponseEntity<PriceModel> updatePrice(@RequestBody PriceModel price) {
        return priceService.updatePrice(price);
    }

    @DeleteMapping("/{id}")
    public void deletePrice(@PathVariable Long id) {
        priceService.deletePriceById(id);
    }
}


