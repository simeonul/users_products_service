package com.simeon.webservices.licenta_backend.repositories;

import com.simeon.webservices.licenta_backend.entities.users.AccountDetails;
import com.simeon.webservices.licenta_backend.entities.users.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountDetailsRepository extends JpaRepository<AccountDetails, UUID> {
    @Query("SELECT ad.userAccount FROM AccountDetails ad WHERE ad.email = :email")
    Optional<UserAccount> findByEmail(@Param("email") String email);
}
