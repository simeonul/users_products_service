package com.simeon.webservices.licenta_backend.services;

import com.simeon.webservices.licenta_backend.clients.FoodFactsProperties;
import com.simeon.webservices.licenta_backend.clients.FoodFactsProxy;
import com.simeon.webservices.licenta_backend.clients.entities.FoodFactsProduct;
import com.simeon.webservices.licenta_backend.clients.entities.FoodFactsResponse;
import com.simeon.webservices.licenta_backend.dtos.mappers.ProductMapper;
import com.simeon.webservices.licenta_backend.entities.CategoryResult;
import com.simeon.webservices.licenta_backend.entities.CheckProductResponse;
import com.simeon.webservices.licenta_backend.entities.ResultType;
import com.simeon.webservices.licenta_backend.entities.ingredients.Ingredient;
import com.simeon.webservices.licenta_backend.entities.products.Product;
import com.simeon.webservices.licenta_backend.entities.users.UserAccount;
import com.simeon.webservices.licenta_backend.exceptions.DuplicateProductException;
import com.simeon.webservices.licenta_backend.exceptions.ProductNotFoundException;
import com.simeon.webservices.licenta_backend.utils.ProcessingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BarcodeSearchService {
    private final FoodFactsProxy foodFactsProxy;
    private final FoodFactsProperties foodFactsProperties;
    private final IngredientService ingredientService;
    private final UserAccountService userAccountService;
    private final ProductService productService;

    @Autowired
    public BarcodeSearchService(
            final FoodFactsProxy foodFactsProxy,
            final FoodFactsProperties foodFactsProperties,
            final IngredientService ingredientService,
            final UserAccountService userAccountService,
            final ProductService productService
    ) {
        this.foodFactsProxy = foodFactsProxy;
        this.foodFactsProperties = foodFactsProperties;
        this.ingredientService = ingredientService;
        this.userAccountService = userAccountService;
        this.productService = productService;
    }

    private FoodFactsResponse getFoodFactsResponse(String barcode) {
        return foodFactsProxy.getProductInfo(
                barcode, String.join(",", foodFactsProperties.getProductSearchFields())
        );
    }


    private ResultType checkOverallSafety(CategoryResult allergicInfo, CategoryResult vegetarianInfo, CategoryResult veganInfo) {
        ResultType allergicSafety = allergicInfo.getIsSafe();
        ResultType vegetarianSafety = vegetarianInfo.getIsSafe();
        ResultType veganSafety = veganInfo.getIsSafe();

        if (ResultType.NO.equals(allergicSafety) || ResultType.NO.equals(vegetarianSafety) || ResultType.NO.equals(veganSafety)) {
            return ResultType.NO;
        } else if (ResultType.MAYBE.equals(allergicSafety) || ResultType.MAYBE.equals(vegetarianSafety) || ResultType.MAYBE.equals(veganSafety)) {
            return ResultType.MAYBE;
        } else {
            return ResultType.YES;
        }
    }

    private CheckProductResponse generateCheckProductResponse(
            String productName,
            String brand,
            UserAccount user,
            List<Ingredient> foodFactsFilteredIngredients
    ) {
        CategoryResult allergicInfo = new CategoryResult(true, ResultType.YES, null);
        CategoryResult vegetarianInfo = new CategoryResult(false, ResultType.YES, null);
        CategoryResult veganInfo = new CategoryResult(false, ResultType.YES, null);

        Set<String> userAllergicIngredientIds = user.getAllergicIngredients().stream()
                .map(Ingredient::getId)
                .collect(Collectors.toSet());
        List<String> allergicIngredients = foodFactsFilteredIngredients.stream()
                .filter(ingredient -> userAllergicIngredientIds.contains(ingredient.getId()))
                .map(Ingredient::getText)
                .collect(Collectors.toList());


        if (!allergicIngredients.isEmpty()) {
            allergicInfo.setIsSafe(ResultType.NO);
            allergicInfo.setProblematicIngredients(allergicIngredients);
        }

        if (user.getVegetarian()) {
            vegetarianInfo.setInclude(true);
            List<String> nonVegetarianIngredients = new ArrayList<>();

            boolean hasBeenSetToNotSafe = false;
            for (Ingredient ingredient : foodFactsFilteredIngredients) {
                Boolean vegetarian = ingredient.getVegetarian();
                if (Boolean.FALSE.equals(vegetarian)) {
                    hasBeenSetToNotSafe = true;
                    vegetarianInfo.setIsSafe(ResultType.NO);
                    nonVegetarianIngredients.add(ingredient.getText());
                } else if (vegetarian == null && !hasBeenSetToNotSafe) {
                    vegetarianInfo.setIsSafe(ResultType.MAYBE);
                    nonVegetarianIngredients.add(ingredient.getText());
                }
            }
            vegetarianInfo.setProblematicIngredients(nonVegetarianIngredients);
        }

        if (user.getVegan()) {
            veganInfo.setInclude(true);
            List<String> nonVeganIngredients = new ArrayList<>();

            boolean hasBeenSetToNotSafe = false;
            for (Ingredient ingredient : foodFactsFilteredIngredients) {
                Boolean vegan = ingredient.getVegan();
                if (Boolean.FALSE.equals(vegan)) {
                    hasBeenSetToNotSafe = true;
                    veganInfo.setIsSafe(ResultType.NO);
                    nonVeganIngredients.add(ingredient.getText());
                } else if (vegan == null && !hasBeenSetToNotSafe) {
                    veganInfo.setIsSafe(ResultType.MAYBE);
                    nonVeganIngredients.add(ingredient.getText());
                }
            }
            veganInfo.setProblematicIngredients(nonVeganIngredients);
        }

        return new CheckProductResponse(
                productName,
                brand,
                checkOverallSafety(allergicInfo, vegetarianInfo, veganInfo),
                allergicInfo,
                vegetarianInfo,
                veganInfo
        );
    }

    public CheckProductResponse checkProductForUser(String email, String barcode) {
        Optional<Product> optionalLocalProduct = productService.getProduct(barcode);
        if (optionalLocalProduct.isPresent()) {
            Product localProduct = optionalLocalProduct.get();
            return generateCheckProductResponse(
                    localProduct.getProductName(),
                    localProduct.getBrand(),
                    userAccountService.findUserEntityByEmail(email),
                    localProduct.getIngredients()
            );
        } else {
            FoodFactsResponse foodFactsResponse = getFoodFactsResponse(barcode);
            if (1 == foodFactsResponse.getStatus() && foodFactsResponse.getProduct().getIngredients() != null) {
                FoodFactsProduct foodFactsProduct = foodFactsResponse.getProduct();
                List<Ingredient> foodFactsFilteredIngredients = ProcessingUtils.convertFilterFoodFactsIngredients(
                        foodFactsProduct.getIngredients()
                );
                ingredientService.insertIngredients(foodFactsFilteredIngredients);

                return generateCheckProductResponse(
                        foodFactsProduct.getProductName(),
                        foodFactsProduct.getBrand(),
                        userAccountService.findUserEntityByEmail(email),
                        ingredientService.replaceWithLocalIngredients(foodFactsFilteredIngredients)
                );
            } else {
                throw new ProductNotFoundException(
                        String.format("Product with barcode {%s} was not found!", barcode)
                );
            }
        }
    }

    public Product findProduct(String barcode) {
        Optional<Product> localProduct = productService.getProduct(barcode);
        if (localProduct.isPresent()) {
            return localProduct.get();
        } else {
            FoodFactsResponse foodFactsResponse = getFoodFactsResponse(barcode);
            if (1 == foodFactsResponse.getStatus() && foodFactsResponse.getProduct().getIngredients() != null) {
                Product product = ProductMapper.mapClientProductToEntity(foodFactsResponse.getProduct(), barcode);
                product.setIngredients(ingredientService.replaceWithLocalIngredients(product.getIngredients()));
                return product;
            }else {
                throw new ProductNotFoundException(
                        String.format("Product with barcode {%s} was not found!", barcode)
                );
            }
        }
    }

    public void insertProduct(Product product) {
        String barcode = product.getBarcode();
        Optional<Product> localProduct = productService.getProduct(barcode);
        if (localProduct.isPresent()) {
            throw new DuplicateProductException(
                    String.format("Product with barcode {%s} already exists!", barcode)
            );
        } else {
            FoodFactsResponse foodFactsResponse = getFoodFactsResponse(barcode);
            if (1 == foodFactsResponse.getStatus() && foodFactsResponse.getProduct().getIngredients() != null) {
                throw new DuplicateProductException(
                        String.format("Product with barcode {%s} already exists!", barcode)
                );
            }else {
                productService.insertProduct(product);
            }
        }
    }
}
