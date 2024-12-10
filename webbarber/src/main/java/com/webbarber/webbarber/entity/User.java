package com.webbarber.webbarber.entity;

import com.webbarber.webbarber.dto.RegisterDTO;
import com.webbarber.webbarber.dto.UserDTO;
import com.webbarber.webbarber.infra.UserRole;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity(name = "user")
@Table(name = "users")
public class User implements UserDetails, Comparable {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private String tel;
    private String password;
    private UserRole role;
    private int amountBookedServices;

    public User(String name, String tel, String password) {
        this.name = name;
        this.tel = tel;
        this.password = password;
        this.role = UserRole.USER;
        this.amountBookedServices = 0;
    }

    public User(@Valid RegisterDTO data) {
        this.name = data.name();
        this.tel = data.tel();
        this.password = data.password();
        this.role = UserRole.USER;
        this.amountBookedServices = 0;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getLogin() { return this.tel; }

    public UserRole getUserRole() { return this.role; }

    public int getAmountBookedServices() { return this.amountBookedServices; }

    public void addAmountBookedServices() { this.amountBookedServices += 1; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == UserRole.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return this.tel;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; //corrigir essa brincadeira
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

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        User oUser = (User) o;
        return this.getName().compareTo(oUser.getName());
    }

    public String toString() {
        return "teste";
    }
}
