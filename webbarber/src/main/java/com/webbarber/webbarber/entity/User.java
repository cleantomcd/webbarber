package com.webbarber.webbarber.entity;

import com.webbarber.webbarber.dto.RegisterDTO;
import com.webbarber.webbarber.infra.UserRole;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Entidade que representa um usuário no sistema. Esta classe implementa a interface {@link UserDetails} para ser utilizada
 * no contexto de autenticação e autorização do Spring Security. O usuário pode ter um papel (role) de ADMIN ou USER.
 * Além disso, a classe mantém o controle da quantidade de serviços agendados pelo usuário.
 */
@Entity(name = "User")
@Table(name = "users")
public class User implements UserDetails, Comparable<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name; // Nome do usuário
    private String phone; // Número de telefone do usuário (usado como login)
    private String password; // Senha do usuário
    private UserRole role; // Papel do usuário, que pode ser ADMIN ou USER
    private int amountBookedServices; // Contador de serviços agendados pelo usuário

    /**
     * Construtor que cria um novo usuário com os dados fornecidos (nome, telefone e senha).
     * O papel do usuário é definido como "USER" por padrão e o contador de serviços agendados é inicializado em 0.
     *
     * @param name Nome do usuário.
     * @param phone Número de telefone do usuário.
     * @param password Senha do usuário.
     */
    public User(String name, String phone, String password) {
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.role = UserRole.USER;
        this.amountBookedServices = 0;
    }

    /**
     * Construtor que cria um novo usuário a partir de um objeto DTO de registro ({@link RegisterDTO}).
     * O papel do usuário é definido como "USER" por padrão e o contador de serviços agendados é inicializado em 0.
     *
     * @param data DTO contendo os dados de nome, telefone e senha.
     */
    public User(@Valid RegisterDTO data) {
        this.name = data.name();
        this.phone = data.phone();
        this.password = data.password();
        this.role = UserRole.USER;
        this.amountBookedServices = 0;
    }

    /**
     * Construtor padrão necessário para a JPA.
     */
    public User() {
    }

    /**
     * Obtém o nome do usuário.
     *
     * @return Nome do usuário.
     */
    public String getName() {
        return name;
    }

    /**
     * Define o nome do usuário.
     *
     * @param name Nome do usuário.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtém o número de telefone do usuário.
     *
     * @return Número de telefone do usuário.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Define o número de telefone do usuário.
     *
     * @param phone Número de telefone do usuário.
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Obtém o login do usuário. Para este sistema, o login é o número de telefone.
     *
     * @return Número de telefone do usuário.
     */
    public String getLogin() {
        return this.phone;
    }

    /**
     * Obtém o papel (role) do usuário, que pode ser ADMIN ou USER.
     *
     * @return O papel do usuário.
     */
    public UserRole getRole() {
        return this.role;
    }

    /**
     * Obtém a quantidade de serviços agendados pelo usuário.
     *
     * @return Quantidade de serviços agendados.
     */
    public int getAmountBookedServices() {
        return this.amountBookedServices;
    }

    /**
     * Incrementa a quantidade de serviços agendados pelo usuário em 1.
     */
    public void addAmountBookedServices() {
        this.amountBookedServices += 1;
    }

    /**
     * Obtém as autoridades (roles) associadas ao usuário.
     * O usuário pode ter a autoridade "ROLE_USER" e, caso seja ADMIN, também terá "ROLE_ADMIN".
     *
     * @return Coleção de autoridades do usuário.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == UserRole.ADMIN) {
            return List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_USER")
            );
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }

    /**
     * Obtém a senha do usuário.
     *
     * @return Senha do usuário.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Obtém o nome de usuário (usado como login). Para este sistema, o nome de usuário é o telefone.
     *
     * @return Número de telefone do usuário.
     */
    @Override
    public String getUsername() {
        return this.phone;
    }

    /**
     * Define o comportamento do usuário para o Spring Security, indicando que a conta nunca expira.
     *
     * @return Sempre retorna true (conta não expira).
     */
    @Override
    public boolean isAccountNonExpired() {
        return true; // Pode ser melhorado conforme as necessidades do sistema.
    }

    /**
     * Define o comportamento do usuário para o Spring Security, indicando que a conta nunca fica bloqueada.
     *
     * @return Sempre retorna true (conta não bloqueada).
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Define o comportamento do usuário para o Spring Security, indicando que as credenciais nunca expiram.
     *
     * @return Sempre retorna true (credenciais não expiram).
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Define o comportamento do usuário para o Spring Security, indicando que a conta está sempre habilitada.
     *
     * @return Sempre retorna true (conta habilitada).
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Define a senha do usuário.
     *
     * @param password Senha do usuário.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Obtém o ID único do usuário.
     *
     * @return ID do usuário.
     */
    public String getId() {
        return id;
    }

    /**
     * Compara este usuário com outro usuário com base no nome.
     *
     * @param o Usuário a ser comparado.
     * @return Resultado da comparação entre os nomes dos usuários.
     */
    @Override
    public int compareTo(@NonNull User o) {
        return this.getName().compareTo(o.getName());
    }

    /**
     * Retorna uma representação em string do usuário.
     *
     * @return Uma string representando o usuário.
     */
    @Override
    public String toString() {
        return "User{id='" + id + "', name='" + name + "', phone='" + phone + "', role=" + role + ", amountBookedServices=" + amountBookedServices + "}";
    }
}
