package com.simeon.webservices.licenta_backend.dtos.mappers;

import com.simeon.webservices.licenta_backend.dtos.UserAccountDto;
import com.simeon.webservices.licenta_backend.entities.users.UserAccount;

public class UserAccountMapper{
    public static UserAccount mapDtoToEntity(UserAccountDto userAccountDto){
        return new UserAccount(
                userAccountDto.getFirstName(),
                userAccountDto.getLastName(),
                AccountDetailsMapper.mapDtoToEntity(userAccountDto.getAccountDetails()),
                userAccountDto.getVegetarian(),
                userAccountDto.getVegan(),
                userAccountDto.getAllergicIngredients(),
                userAccountDto.getSafeProducts()
        );
    }

    public static UserAccountDto mapEntityToDto(UserAccount userAccount){
        return new UserAccountDto(
                userAccount.getId(),
                userAccount.getFirstName(),
                userAccount.getLastName(),
                AccountDetailsMapper.mapEntityToDto(userAccount.getAccountDetails()),
                userAccount.getVegetarian(),
                userAccount.getVegan(),
                userAccount.getAllergicIngredients(),
                userAccount.getSafeProducts()
        );
    }
}
