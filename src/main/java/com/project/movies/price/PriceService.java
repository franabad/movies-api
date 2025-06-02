package com.project.movies.price;

import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.HttpStatus.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

//TO DO: Implement exceptions like create a price that already exist
@Service
public class PriceService {

    @Autowired
    private IPriceRepository priceRepository;

    @Transactional(readOnly = true)
    public ResponseEntity<List<PriceModel>> getAllPrices() {
        return ResponseEntity.status(OK).body(priceRepository.findAll());
    }

    @Transactional
    public ResponseEntity<PriceModel> createPrice(@RequestBody PriceModel price) {
        return ResponseEntity.status(CREATED).body(priceRepository.save(price));
    }

    @Transactional
    public ResponseEntity<PriceModel> updatePrice(@RequestBody PriceModel price) {
        return ResponseEntity.status(OK).body(priceRepository.save(price));
    }

    // TO DO: Add validation that the price exists
    @Transactional
    public void deletePriceById(@PathVariable Long id) {
        priceRepository.deleteById(id);
    }
}
