package com.simeon.webservices.licenta_backend.dtos.mappers;

import com.simeon.webservices.licenta_backend.dtos.AccountDetailsDto;
import com.simeon.webservices.licenta_backend.entities.users.AccountDetails;

public class AccountDetailsMapper {
    public static AccountDetails mapDtoToEntity(AccountDetailsDto accountDetailsDto){
        return new AccountDetails(
                accountDetailsDto.getEmail(),
                accountDetailsDto.getPassword(),
                accountDetailsDto.getRole()
        );
    }

    public static AccountDetailsDto mapEntityToDto(AccountDetails accountDetails){
        return new AccountDetailsDto(
                accountDetails.getId(),
                accountDetails.getEmail(),
                accountDetails.getPassword(),
                accountDetails.getRole()
        );
    }
}
