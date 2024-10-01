package com.simeon.webservices.licenta_backend.controllers;

import com.simeon.webservices.licenta_backend.dtos.UserAccountDto;
import com.simeon.webservices.licenta_backend.services.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.lang.String.format;


@RestController
public class AuthenticationController {

    private final UserAccountService userAccountService;

    @Autowired
    public AuthenticationController(
            final UserAccountService userAccountService
    ) {
        this.userAccountService = userAccountService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserAccountDto userAccountDto) {
        try {
            userAccountService.insertUserAccount(userAccountDto);
        }catch (Exception ex){
            throw new RuntimeException(
                    format("Email {%s} is already in use!", userAccountDto.getAccountDetails().getEmail())
            );
        }
        return new ResponseEntity<>("User account created successfully!", HttpStatus.CREATED);
    }

    @RequestMapping("/LogIn")
    public UserAccountDto getUserAfterLogin(Authentication authentication){
        return userAccountService.findUserByEmail(authentication.getName());
    }


}
