package com.simeon.webservices.licenta_backend.dtos.mappers;

import com.simeon.webservices.licenta_backend.clients.entities.FoodFactsProduct;
import com.simeon.webservices.licenta_backend.entities.products.Product;
import com.simeon.webservices.licenta_backend.utils.ProcessingUtils;

public class ProductMapper {
    public static Product mapClientProductToEntity(FoodFactsProduct product, String barcode) {
        return new Product(
                barcode,
                ProcessingUtils.convertFilterFoodFactsIngredients(product.getIngredients()),
                product.getProductName(),
                product.getBrand()
        );
    }
}
