package com.webbarber.webbarber.entity;

import com.webbarber.webbarber.dto.UserDTO;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity(name = "user")
@Table(name = "users")
public class User implements UserDetails {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String tel;
    private String password;
    private String login;

    public User(String name, String tel, String password) {
        this.name = name;
        this.tel = tel;
        this.password = password;
        this.login = tel;
    }

    public User() {
    }

    public User(UserDTO data) {
        this.name = data.name();
        this.tel = data.tel();
        this.password = data.password();
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
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

    public Long getId() {
        return id;
    }

}
