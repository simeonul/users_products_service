package com.simeon.webservices.licenta_backend.configurations;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/register").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/LogIn").authenticated()
                        .requestMatchers("/search/**").hasAnyAuthority("CLIENT", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/ingredients/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/ingredients").hasAnyAuthority("CLIENT", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/ingredients/{ingredientId}").hasAuthority("ADMIN")
                        .requestMatchers("/products/**").hasAuthority("ADMIN")
                        .requestMatchers("/users/**").hasAnyAuthority("CLIENT", "ADMIN"))
                .formLogin(withDefaults())
                .httpBasic(withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterAfter(new JWTGeneratorFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new JWTValidatorFilter(), BasicAuthenticationFilter.class)
                .cors(cors -> cors.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration configuration = new CorsConfiguration();
                        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setExposedHeaders(Arrays.asList("Authorization"));
                        configuration.setMaxAge(3600L);
                        return configuration;
                    }
                }));
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
