package com.simeon.webservices.licenta_backend.configurations;

import com.simeon.webservices.licenta_backend.dtos.UserAccountDto;
import com.simeon.webservices.licenta_backend.services.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CredentialsAuthenticationProvider implements AuthenticationProvider {

    private UserAccountService userService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public CredentialsAuthenticationProvider(UserAccountService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        UserAccountDto databaseUser = userService.findUserByEmail(email);
        if(passwordEncoder.matches(password, databaseUser.getAccountDetails().getPassword())){
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(databaseUser.getAccountDetails().getRole().toString()));
            return new UsernamePasswordAuthenticationToken(email, password, authorities);
        }else{
            throw new BadCredentialsException("Invalid password!");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
