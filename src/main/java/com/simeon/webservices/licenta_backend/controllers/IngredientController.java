package com.simeon.webservices.licenta_backend.controllers;

import com.simeon.webservices.licenta_backend.entities.ingredients.Ingredient;
import com.simeon.webservices.licenta_backend.services.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    @Autowired
    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping("/{containsString}")
    public List<Ingredient> getIngredientsContains(
            final @PathVariable String containsString
    ){
        return ingredientService.getIngredientsContains(containsString);
    }

    @PostMapping
    public ResponseEntity<Void> addIngredient(
            final @RequestBody Ingredient ingredient
    ){
        ingredientService.addIngredientFromLocal(ingredient);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PutMapping("/{ingredientId}")
    public ResponseEntity<Void> updateIngredient(
            final @PathVariable String ingredientId,
            final @RequestBody Ingredient newIngredient
    ){
        ingredientService.updateIngredient(ingredientId, newIngredient);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
