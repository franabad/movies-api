package com.project.movies.price;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4000")
@RestController
@RequestMapping("/api/prices")
public class PriceController {

    @Autowired
    private IPriceRepository priceRepository;

    @GetMapping
    public List<PriceModel> getPrices() {
        return priceRepository.findAll();
    }

    @PutMapping
    public PriceModel updatePrice(@RequestBody PriceModel price) {
        return priceRepository.save(price);
    }

    @DeleteMapping("/{id}")
    public void deletePrice(@PathVariable Long id) {
        priceRepository.deleteById(id);
    }

    @PostMapping
    public PriceModel createPrice(@RequestBody PriceModel price) {
        return priceRepository.save(price);
    }
}


