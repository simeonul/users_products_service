package com.simeon.webservices.licenta_backend.clients.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class FoodFactsProduct {
    private List<FoodFactsIngredient> ingredients;

    @JsonProperty("ingredients_tags")
    private String[] ingredientIdentifiers;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("brands")
    private String brand;
}
