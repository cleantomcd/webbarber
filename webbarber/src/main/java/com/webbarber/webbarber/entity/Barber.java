package com.webbarber.webbarber.entity;

import com.webbarber.webbarber.infra.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Entidade que representa um barbeiro no sistema, com informações sobre nome, telefone, senha e papel (role).
 * Esta classe implementa a interface {@link UserDetails} para ser usada na autenticação e autorização do Spring Security.
 *
 * @see UserRole
 */
@Entity(name = "Barber")
@Table(name = "barbers")
public class Barber implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private String phone;
    private String password;
    private UserRole role;

    /**
     * Construtor padrão necessário para a JPA.
     */
    public Barber() {

    }

    /**
     * Construtor principal da classe.
     *
     * @param name Nome do barbeiro, com pelo menos 3 caracteres.
     * @param formatedPhoneNumber Número de telefone do barbeiro.
     * @param encryptedPassword Senha do barbeiro, já criptografada.
     */
    public Barber(@Pattern(regexp = "^.{3,}$", message = "Seu nome deve ter pelo menos 3 caracteres") String name, String formatedPhoneNumber, String encryptedPassword) {
        this.name = name;
        this.phone = formatedPhoneNumber;
        this.password = encryptedPassword;
        this.role = UserRole.ADMIN; // O barbeiro é um ADMIN por padrão.
    }

    /**
     * Obtém o ID do barbeiro.
     *
     * @return O ID do barbeiro.
     */
    public String getId() {
        return id;
    }

    /**
     * Obtém o nome do barbeiro.
     *
     * @return O nome do barbeiro.
     */
    public String getName() {
        return name;
    }

    /**
     * Define o nome do barbeiro.
     *
     * @param name O novo nome do barbeiro.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtém o número de telefone do barbeiro.
     *
     * @return O número de telefone do barbeiro.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Define o número de telefone do barbeiro.
     *
     * @param phone O novo número de telefone do barbeiro.
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Obtém as autoridades (roles) do barbeiro.
     *
     * @return Coleção de {@link GrantedAuthority}, representando os papéis do barbeiro.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
    }

    /**
     * Obtém a senha do barbeiro.
     *
     * @return A senha do barbeiro.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Obtém o nome de usuário do barbeiro. Aqui, usamos o telefone como nome de usuário.
     *
     * @return O número de telefone do barbeiro.
     */
    @Override
    public String getUsername() {
        return this.phone;
    }

    /**
     * Verifica se a conta do barbeiro não expirou.
     *
     * @return Sempre retorna true, pois não há expiração de conta no modelo.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true; // Corrigir esse comportamento se necessário.
    }

    /**
     * Verifica se a conta do barbeiro não está bloqueada.
     *
     * @return Sempre retorna true, pois não há bloqueio de conta no modelo.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Verifica se as credenciais do barbeiro não expiraram.
     *
     * @return Sempre retorna true, pois não há expiração de credenciais no modelo.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Verifica se a conta do barbeiro está habilitada.
     *
     * @return Sempre retorna true, pois não há lógica de habilitação no modelo.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Define a senha do barbeiro.
     *
     * @param password A nova senha do barbeiro.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Obtém o papel (role) do barbeiro.
     *
     * @return O papel (role) do barbeiro.
     */
    public UserRole getRole() {
        return role;
    }
}
