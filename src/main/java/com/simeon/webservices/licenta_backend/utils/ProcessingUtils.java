package com.simeon.webservices.licenta_backend.utils;

import com.simeon.webservices.licenta_backend.clients.entities.FoodFactsIngredient;
import com.simeon.webservices.licenta_backend.dtos.mappers.IngredientMapper;
import com.simeon.webservices.licenta_backend.entities.ingredients.Ingredient;

import java.util.List;
import java.util.stream.Collectors;

public class ProcessingUtils {

    public static List<Ingredient> convertFilterFoodFactsIngredients(
            List<FoodFactsIngredient> ingredients
    ) {
        return ingredients.stream()
                .filter(ingredient -> ingredient.getId().startsWith("en:"))
                .map(IngredientMapper::mapClientIngredientToEntity)
                .collect(Collectors.toList());
    }
}
