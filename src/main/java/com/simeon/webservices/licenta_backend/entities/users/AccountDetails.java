package com.simeon.webservices.licenta_backend.entities.users;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "account_details")
public class AccountDetails {
    @Id
    @Column(name = "user_account_id")
    private UUID id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private AccountRole role;

    @OneToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "user_account_id")
    private UserAccount userAccount;

    public AccountDetails() {
    }

    public AccountDetails(UUID id, String email, String password, AccountRole role, UserAccount userAccount) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.userAccount = userAccount;
    }

    public AccountDetails(String email, String password, AccountRole role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AccountRole getRole() {
        return role;
    }

    public void setRole(AccountRole role) {
        this.role = role;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }
}
