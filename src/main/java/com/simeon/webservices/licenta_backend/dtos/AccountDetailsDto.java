package com.simeon.webservices.licenta_backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.simeon.webservices.licenta_backend.entities.users.AccountRole;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class AccountDetailsDto {
    private UUID id;
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private AccountRole role;
}
