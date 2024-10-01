package com.simeon.webservices.licenta_backend.clients.entities;

import lombok.Data;

@Data
public class FoodFactsIngredient {
    private String id;
    private String text;
    private String vegan;
    private String vegetarian;
}
