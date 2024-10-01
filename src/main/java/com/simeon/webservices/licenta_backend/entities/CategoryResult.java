package com.simeon.webservices.licenta_backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class CategoryResult {
    private boolean include;
    private ResultType isSafe;
    private List<String> problematicIngredients;
}
