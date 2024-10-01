package com.simeon.webservices.licenta_backend.controllers;

import com.simeon.webservices.licenta_backend.entities.products.Product;
import com.simeon.webservices.licenta_backend.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @PutMapping
    public ResponseEntity<Void> updateProduct(
            @RequestBody Product product
    ) {
        productService.updateProduct(product);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{barcode}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable String barcode
    ) {
        productService.deleteProduct(barcode);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
