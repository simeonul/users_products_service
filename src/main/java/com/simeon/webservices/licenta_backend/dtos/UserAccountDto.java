package com.simeon.webservices.licenta_backend.dtos;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.simeon.webservices.licenta_backend.entities.SafeProduct;
import com.simeon.webservices.licenta_backend.entities.ingredients.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;
    private String firstName;
    private String lastName;
    private AccountDetailsDto accountDetails;
    private Boolean vegetarian;
    private Boolean vegan;
    private List<Ingredient> allergicIngredients;
    private Set<SafeProduct> safeProducts;

    public UserAccountDto(
            UUID id,
            String firstName,
            String lastName,
            AccountDetailsDto accountDetails,
            boolean vegetarian,
            boolean vegan,
            List<Ingredient> allergicIngredients,
            Set<SafeProduct> safeProducts
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.accountDetails = accountDetails;
        this.vegetarian = vegetarian;
        this.vegan = vegan;
        this.allergicIngredients = allergicIngredients;
        this.safeProducts = safeProducts;
    }
}
