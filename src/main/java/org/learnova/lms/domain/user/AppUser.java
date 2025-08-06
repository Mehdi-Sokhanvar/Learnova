package org.learnova.lms.domain.user;


import org.learnova.lms.domain.base.BaseEntity;
import org.learnova.lms.domain.enums.Status;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(indexes = {
        @Index(name = "idx_user_email", columnList = "email"),
        @Index(name = "idx_user_username", columnList = "userName")
})

//todo : why use better use index
public class AppUser extends BaseEntity<Long> implements UserDetails {


    @NotBlank
    @Column(nullable = false)
    @Size(max = 100)
    private String userName;


    @NotBlank
    @Column(nullable = false)
    private String password;


    @NotBlank
    @Column(nullable = false)
    @Size(max = 255)
    private String email;


    private String firstName;


    private String lastName;


    private String phone;

    @Embedded
    private Address address;


    @Enumerated(EnumType.STRING)
    private Status status=Status.PENDING;

    @ManyToOne()
    @JoinColumn(name="role_id")
    private Role role;

    public AppUser() {

    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(getRole().getName()));
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public Address getAddress() {
        return address;
    }

    public Status getStatus() {
        return status;
    }

    public AppUser(String userName, String password, String email, Role role) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.role = role;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AppUser appUser = (AppUser) o;
        return Objects.equals(userName, appUser.userName) && Objects.equals(password, appUser.password) && Objects.equals(email, appUser.email) && Objects.equals(firstName, appUser.firstName) && Objects.equals(lastName, appUser.lastName) && Objects.equals(phone, appUser.phone) && Objects.equals(address, appUser.address) && status == appUser.status && Objects.equals(role, appUser.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, password, email, firstName, lastName, phone, address, status, role);
    }
}

