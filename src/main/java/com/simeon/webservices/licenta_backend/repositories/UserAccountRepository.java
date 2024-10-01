package com.simeon.webservices.licenta_backend.repositories;

import com.simeon.webservices.licenta_backend.entities.users.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, UUID> {
}
