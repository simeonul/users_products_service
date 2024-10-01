package com.simeon.webservices.licenta_backend.services;

import com.simeon.webservices.licenta_backend.dtos.UserAccountDto;
import com.simeon.webservices.licenta_backend.dtos.mappers.UserAccountMapper;
import com.simeon.webservices.licenta_backend.entities.SafeProduct;
import com.simeon.webservices.licenta_backend.entities.ingredients.Ingredient;
import com.simeon.webservices.licenta_backend.entities.users.UserAccount;
import com.simeon.webservices.licenta_backend.repositories.AccountDetailsRepository;
import com.simeon.webservices.licenta_backend.repositories.UserAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
public class UserAccountService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserAccountService.class);
    private final UserAccountRepository userAccountRepository;
    private final AccountDetailsRepository accountDetailsRepository;
    private final PasswordEncoder passwordEncoder;
    private final IngredientService ingredientService;

    @Autowired
    public UserAccountService(
            final UserAccountRepository userAccountRepository,
            final AccountDetailsRepository accountDetailsRepository,
            final PasswordEncoder passwordEncoder,
            final IngredientService ingredientService
    ) {
        this.userAccountRepository = userAccountRepository;
        this.accountDetailsRepository = accountDetailsRepository;
        this.passwordEncoder = passwordEncoder;
        this.ingredientService = ingredientService;
    }

    public void insertUserAccount(UserAccountDto userAccountDto) {
        try {
            userAccountDto.getAccountDetails().setPassword(passwordEncoder.encode(userAccountDto.getAccountDetails().getPassword()));
            UserAccount userAccount = UserAccountMapper.mapDtoToEntity(userAccountDto);
            userAccount.setAccountDetails(userAccount.getAccountDetails());
            this.userAccountRepository.save(userAccount);
        } catch (Exception ex){
            throw new RuntimeException(ex);
        }

    }

    public UserAccount findUserEntityById(UUID userId) {
        return userAccountRepository.findById(userId)
                .orElseThrow(() -> {
                    LOGGER.error("UserAccount with id {} was not found in the database", userId);
                    return new EntityNotFoundException(
                            format("UserAccount with id {%s} was not found in the database", userId)
                    );
                });
    }

    public UserAccount findUserEntityByEmail(String email) {
        return accountDetailsRepository.findByEmail(email)
                .orElseThrow(() -> {
                    LOGGER.error("UserAccount with email {} was not found in the database", email);
                    return new EntityNotFoundException(
                            format("UserAccount with email {%s} was not found in the database", email)
                    );
                });
    }

    public UserAccountDto findUserByEmail(String email) {
        return accountDetailsRepository.findByEmail(email)
                .map(UserAccountMapper::mapEntityToDto)
                .orElseThrow(() -> {
                    LOGGER.error("User with email {} was not found in the database", email);
                    return new EntityNotFoundException(
                            format("UserAccount with email {%s} was not found in the database", email)
                    );
                });
    }


    public void addAllergicIngredient(String email, String allergicIngredientId) {
        UserAccount user = findUserEntityByEmail(email);
        Ingredient ingredient = ingredientService.findIngredientEntityById(allergicIngredientId);
        user.getAllergicIngredients().add(ingredient);
        userAccountRepository.save(user);
    }

    public void removeAllergicIngredient(String email, String allergicIngredientId) {
        UserAccount user = findUserEntityByEmail(email);
        Ingredient ingredient = ingredientService.findIngredientEntityById(allergicIngredientId);
        user.getAllergicIngredients().remove(ingredient);
        userAccountRepository.save(user);
    }

    public void changeDietaryRestriction(String email, int restriction) {
        UserAccount user = findUserEntityByEmail(email);
        boolean vegetarian = false;
        boolean vegan = false;
        switch (restriction) {
            case 1:
                vegetarian = true;
                break;
            case 2:
                vegetarian = true;
                vegan = true;
        }
        user.setVegetarian(vegetarian);
        user.setVegan(vegan);
        userAccountRepository.save(user);
    }

    public void addSafeProduct(String email, SafeProduct safeProduct) {
        UserAccount user = findUserEntityByEmail(email);
        user.getSafeProducts().add(safeProduct);
        userAccountRepository.save(user);
    }

    public void removeSafeProduct(String email, String safeProductBarcode) {
        UserAccount user = findUserEntityByEmail(email);
        user.getSafeProducts().removeIf(safeProduct -> safeProduct.getBarcode().equals(safeProductBarcode));
        userAccountRepository.save(user);
    }

    public List<UserAccount> getAllUsersWithSafeProduct(String barcode) {
        return userAccountRepository.findAll().stream()
                .filter(user -> user.getSafeProducts().stream()
                        .anyMatch(safeProduct -> safeProduct.getBarcode().equals(barcode)))
                .collect(Collectors.toList());
    }

    public List<UserAccountDto> getAllUserAccounts() {
        return userAccountRepository.findAll().stream().map(
                UserAccountMapper::mapEntityToDto
        ).collect(Collectors.toList());
    }
}
