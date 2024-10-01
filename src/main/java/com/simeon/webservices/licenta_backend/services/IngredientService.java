package com.simeon.webservices.licenta_backend.services;

import com.simeon.webservices.licenta_backend.clients.entities.FoodFactsIngredient;
import com.simeon.webservices.licenta_backend.entities.ingredients.Ingredient;
import com.simeon.webservices.licenta_backend.exceptions.DuplicateIngredientException;
import com.simeon.webservices.licenta_backend.repositories.IngredientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
public class IngredientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IngredientService.class);
    private final IngredientRepository ingredientRepository;

    @Autowired
    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public void insertIngredients(List<Ingredient> ingredients) {
        List<String> ingredientIds = ingredients.stream()
                .map(Ingredient::getId)
                .collect(Collectors.toList());
        Set<String> existingIngredientIds = ingredientRepository.findAllByIdIn(ingredientIds).stream()
                .map(Ingredient::getId)
                .collect(Collectors.toSet());
        List<Ingredient> newIngredients = ingredients.stream()
                .filter(ingredient -> !existingIngredientIds.contains(ingredient.getId()))
                .collect(Collectors.toList());
        try {
            ingredientRepository.saveAll(newIngredients);
        } catch (Exception ex) {
            LOGGER.error("Error occurred while inserting ingredients: {}", ex.getMessage());
        }
    }

    public void addIngredientFromLocal(Ingredient ingredient){
        if(ingredientRepository.findById(ingredient.getId()).isPresent()){
            throw new DuplicateIngredientException(
                    format("Ingredient with id {} already exists", ingredient.getId())
            );
        }else {
            ingredientRepository.save(ingredient);
        }
    }

    public void updateIngredient(String ingredientId, Ingredient newIngredient){
        Ingredient existingIngredient = findIngredientEntityById(ingredientId);
        existingIngredient.setText(newIngredient.getText());
        existingIngredient.setVegetarian(newIngredient.getVegetarian());
        existingIngredient.setVegan(newIngredient.getVegan());
        ingredientRepository.save(existingIngredient);
    }

    public Ingredient findIngredientEntityById(String id) {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("Ingredient with id {} was not found in the database", id);
                    return new EntityNotFoundException(
                            format("Ingredient with id {%s} was not found in the database", id)
                    );
                });
    }

    public List<Ingredient> getIngredientsContains(String containsString) {
        return ingredientRepository.findIngredientsContainingId(containsString);
    }

    public List<Ingredient> replaceWithLocalIngredients(List<Ingredient> foodFactsFilteredIngredients) {
        Map<String, Ingredient> existingIngredientsMap = ingredientRepository.findAllByIdIn(
                        foodFactsFilteredIngredients.stream()
                                .map(Ingredient::getId)
                                .collect(Collectors.toList())).stream()
                .collect(Collectors.toMap(Ingredient::getId, ingredient -> ingredient));
        return foodFactsFilteredIngredients.stream()
                .map(ingredient -> existingIngredientsMap.getOrDefault(ingredient.getId(), ingredient))
                .collect(Collectors.toList());
    }
}
