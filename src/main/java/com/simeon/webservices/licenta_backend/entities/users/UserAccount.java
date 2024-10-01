package com.simeon.webservices.licenta_backend.entities.users;

import com.simeon.webservices.licenta_backend.entities.SafeProduct;
import com.simeon.webservices.licenta_backend.entities.ingredients.Ingredient;
import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "user_accounts")
public class UserAccount {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @OneToOne(
            mappedBy = "userAccount", optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL
    )
    @PrimaryKeyJoinColumn
    private AccountDetails accountDetails;

    @Column(name = "vegetarian", nullable = false)
    private Boolean vegetarian;

    @Column(name = "vegan", nullable = false)
    private Boolean vegan;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_allergic_ingredients",
            joinColumns = @JoinColumn(name = "user_account_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private List<Ingredient> allergicIngredients;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "user_safe_products",
            joinColumns = @JoinColumn(name = "user_account_id")
    )
    private Set<SafeProduct> safeProducts;

    public UserAccount() {
    }

    public UserAccount(UUID id, String firstName, String lastName, AccountDetails accountDetails) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.accountDetails = accountDetails;
        this.allergicIngredients = new ArrayList<>() {
        };
    }


    public UserAccount(
            String firstName,
            String lastName,
            AccountDetails accountDetails,
            boolean vegetarian,
            boolean vegan,
            List<Ingredient> allergicIngredients,
            Set<SafeProduct> safeProducts
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.accountDetails = accountDetails;
        this.vegetarian = vegetarian;
        this.vegan = vegan;
        this.allergicIngredients = allergicIngredients;
        this.safeProducts = safeProducts;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Boolean getVegetarian() {
        return vegetarian;
    }

    public void setVegetarian(Boolean vegetarian) {
        this.vegetarian = vegetarian;
    }

    public AccountDetails getAccountDetails() {
        return accountDetails;
    }

    public void setAccountDetails(AccountDetails accountDetails) {
        if (accountDetails == null) {
            if (this.accountDetails != null) {
                this.accountDetails.setUserAccount(null);
            }
        } else {
            accountDetails.setUserAccount(this);
        }
        this.accountDetails = accountDetails;
    }

    public List<Ingredient> getAllergicIngredients() {
        return allergicIngredients;
    }

    public void setAllergicIngredients(List<Ingredient> allergicIngredients) {
        this.allergicIngredients = allergicIngredients;
    }

    public Boolean getVegan() {
        return vegan;
    }

    public void setVegan(Boolean vegan) {
        this.vegan = vegan;
    }

    public Set<SafeProduct> getSafeProducts() {
        return safeProducts;
    }

    public void setSafeProducts(Set<SafeProduct> safeProducts) {
        this.safeProducts = safeProducts;
    }
}
