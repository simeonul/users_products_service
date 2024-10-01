package com.simeon.webservices.licenta_backend.repositories;

import com.simeon.webservices.licenta_backend.entities.ingredients.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, String> {
    List<Ingredient> findAllByIdIn(List<String> ingredientIds);

    @Query("SELECT i FROM Ingredient i WHERE i.id LIKE %:searchString%")
    List<Ingredient> findIngredientsContainingId(String searchString);
}
