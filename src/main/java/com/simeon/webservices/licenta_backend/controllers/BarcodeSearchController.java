package com.simeon.webservices.licenta_backend.controllers;

import com.simeon.webservices.licenta_backend.entities.CheckProductResponse;
import com.simeon.webservices.licenta_backend.entities.products.Product;
import com.simeon.webservices.licenta_backend.services.BarcodeSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search")
public class BarcodeSearchController {
    private final BarcodeSearchService barcodeSearchService;

    @Autowired
    public BarcodeSearchController(BarcodeSearchService barcodeSearchService){
        this.barcodeSearchService = barcodeSearchService;
    }

    @GetMapping("/{barcode}")
    public ResponseEntity<Product> getProduct(
            @PathVariable String barcode
    ){
        return new ResponseEntity<>(
                barcodeSearchService.findProduct(barcode),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<Void> addNewProduct(
            @RequestBody Product product
    ) {
        barcodeSearchService.insertProduct(product);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/{email}/{barcode}")
    public ResponseEntity<CheckProductResponse> checkProductForUser(
            final @PathVariable String email,
            final @PathVariable String barcode
    ){
        return new ResponseEntity<>(
                barcodeSearchService.checkProductForUser(email, barcode),
                HttpStatus.OK
        );
    }
}
