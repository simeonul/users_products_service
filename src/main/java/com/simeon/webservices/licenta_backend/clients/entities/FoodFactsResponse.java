package com.simeon.webservices.licenta_backend.clients.entities;

import lombok.Data;

@Data
public class FoodFactsResponse {
    private FoodFactsProduct product;

    private String code;

    private int status;
}
