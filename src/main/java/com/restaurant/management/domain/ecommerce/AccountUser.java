package com.restaurant.management.domain.ecommerce;

import com.restaurant.management.domain.layout.Settings;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "account_users")
public class AccountUser extends AbstractUser {

    @NotBlank
    @Size(max = 100)
    @Column(name = "password")
    private String password;

    @Column(name = "password_reset_token")
    private String passwordResetToken;

    @Column(name = "email_verification_token")
    private String emailVerificationToken;

    @Column(name = "isActive")
    private Boolean isActive;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "account_users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @NotAudited
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Company company;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private AccountUserAddress accountUserAddress;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Settings settings;

    public AccountUser() {
    }

    public AccountUser(String name, String lastName, String email,
                       String phone, String jobTitle, String password,
                       String emailVerificationToken,
                       Boolean isActive, Set<Role> roles, Company company,
                       AccountUserAddress accountUserAddress, Settings settings) {
        super(name, lastName, email, phone, jobTitle);
        this.password = password;
        this.emailVerificationToken = emailVerificationToken;
        this.isActive = isActive;
        this.roles = roles;
        this.company = company;
        this.accountUserAddress = accountUserAddress;
        this.settings = settings;
    }

    public AccountUser(Long createdAt, Long updatedAt, String createdByUserId, String updatedByUserId,
                       Long id, String name, String lastName, String email, String phone, String jobTitle,
                       String emailVerificationToken,
                       Boolean isActive, Set<Role> roles) {
        super(createdAt, updatedAt, createdByUserId, updatedByUserId, id, name, lastName, email, phone, jobTitle);
        this.emailVerificationToken = emailVerificationToken;
        this.isActive = isActive;
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordResetToken() {
        return passwordResetToken;
    }

    public void setPasswordResetToken(String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }

    public String getEmailVerificationToken() {
        return emailVerificationToken;
    }

    public void setEmailVerificationToken(String emailVerificationToken) {
        this.emailVerificationToken = emailVerificationToken;
    }

    public Boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public AccountUserAddress getAccountUserAddress() {
        return accountUserAddress;
    }

    public void setAccountUserAddress(AccountUserAddress accountUserAddress) {
        this.accountUserAddress = accountUserAddress;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

}
