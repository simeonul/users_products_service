package com.simeon.webservices.licenta_backend.controllers;

import com.simeon.webservices.licenta_backend.dtos.UserAccountDto;
import com.simeon.webservices.licenta_backend.entities.SafeProduct;
import com.simeon.webservices.licenta_backend.services.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserAccountController {
    private final UserAccountService userAccountService;

    @Autowired
    public UserAccountController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @PostMapping()
    public ResponseEntity<Void> createNewUserAccount(@RequestBody UserAccountDto userAccountDto) {
        userAccountService.insertUserAccount(userAccountDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<UserAccountDto>> getAllUsersAccounts() {
        List<UserAccountDto> userAccounts = userAccountService.getAllUserAccounts();
        return new ResponseEntity<>(userAccounts, HttpStatus.OK);
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserAccountDto> getUserAccount(
            @PathVariable String email
    ) {
        UserAccountDto userAccount = userAccountService.findUserByEmail(email);
        return new ResponseEntity<>(userAccount, HttpStatus.OK);
    }

    @PatchMapping("/add-allergen/{email}")
    public ResponseEntity<Void> addAllergicIngredient(
            @PathVariable String email,
            @RequestParam String allergicIngredientId
    ) {
        userAccountService.addAllergicIngredient(email, allergicIngredientId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/remove-allergen/{email}")
    public ResponseEntity<Void> removeAllergicIngredient(
            @PathVariable String email,
            @RequestParam String allergicIngredientId
    ) {
        userAccountService.removeAllergicIngredient(email, allergicIngredientId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/change-restrictions/{email}/{restriction}")
    public ResponseEntity<Void> changeDietaryRestriction(
            @PathVariable String email,
            @PathVariable int restriction
    ) {
        userAccountService.changeDietaryRestriction(email, restriction);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/add-safe-product/{email}")
    public ResponseEntity<Void> addSafeProduct(
            @PathVariable String email,
            @RequestBody SafeProduct safeProduct
    ) {
        userAccountService.addSafeProduct(email, safeProduct);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/remove-safe-product/{email}/{safeProductBarcode}")
    public ResponseEntity<Void> removeSafeProduct(
            @PathVariable String email,
            @PathVariable String safeProductBarcode
    ) {
        userAccountService.removeSafeProduct(email, safeProductBarcode);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
