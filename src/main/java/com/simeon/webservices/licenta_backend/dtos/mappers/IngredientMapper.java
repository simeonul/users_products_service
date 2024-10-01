package com.simeon.webservices.licenta_backend.dtos.mappers;

import com.simeon.webservices.licenta_backend.clients.entities.FoodFactsIngredient;
import com.simeon.webservices.licenta_backend.entities.ingredients.Ingredient;

public class IngredientMapper {
    private static Boolean parseYesNo(String value) {
        if (value == null) {
            return null;
        } else if (value.equalsIgnoreCase("yes")) {
            return true;
        }
        return false;
    }

    public static Ingredient mapClientIngredientToEntity(FoodFactsIngredient ingredient){
        String identifier = ingredient.getId().substring(3);
        return new Ingredient(
                identifier,
                identifier.replace("-", " "),
                parseYesNo(ingredient.getVegan()),
                parseYesNo(ingredient.getVegetarian())
        );
    }
}
