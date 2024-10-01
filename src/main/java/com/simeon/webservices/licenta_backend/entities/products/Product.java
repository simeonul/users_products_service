package com.simeon.webservices.licenta_backend.entities.products;

import com.simeon.webservices.licenta_backend.entities.ingredients.Ingredient;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @Column(name = "barcode")
    private String barcode;

    @ManyToMany
    @JoinTable(
            name = "product_ingredients",
            joinColumns = @JoinColumn(name = "product_barcode"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private List<Ingredient> ingredients;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "brand")
    private String brand;

    public Product() {
    }

    public Product(String barcode, List<Ingredient> ingredients, String productName, String brand) {
        this.barcode = barcode;
        this.ingredients = ingredients;
        this.productName = productName;
        this.brand = brand;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
