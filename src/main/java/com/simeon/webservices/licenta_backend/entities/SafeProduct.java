package com.simeon.webservices.licenta_backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class SafeProduct {
    @Column(name = "barcode", nullable = false)
    private String barcode;
    @Column(name = "product_name")
    private String productName;
    @Column(name = "brand")
    private String brand;
}