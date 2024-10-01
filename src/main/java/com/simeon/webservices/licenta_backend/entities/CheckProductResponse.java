package com.simeon.webservices.licenta_backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckProductResponse{
    private String productName;
    private String brandName;
    private ResultType isOverallSafe;
    private CategoryResult allergicInfo;
    private CategoryResult vegetarianInfo;
    private CategoryResult veganInfo;
}
