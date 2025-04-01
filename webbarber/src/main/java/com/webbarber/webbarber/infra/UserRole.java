package com.webbarber.webbarber.infra;

/**
 * Enum que define os papéis de usuário dentro do sistema.
 * <p>
 * A classe {@link UserRole} define os dois papéis principais do sistema:
 * <ul>
 *     <li>ADMIN - Representa o papel de administrador.</li>
 *     <li>USER - Representa o papel de usuário comum.</li>
 * </ul>
 * </p>
 */
public enum UserRole {

    /**
     * Papel de administrador. Usuários com este papel têm permissões elevadas.
     */
    ADMIN("ADMIN"),

    /**
     * Papel de usuário comum. Usuários com este papel possuem permissões limitadas.
     */
    USER("USER");

    private final String role;

    /**
     * Construtor do enum {@link UserRole}.
     *
     * @param role O valor do papel do usuário.
     */
    UserRole(String role) {
        this.role = role;
    }

    /**
     * Retorna o valor do papel do usuário.
     *
     * @return O valor do papel do usuário.
     */
    public String getRole() {
        return this.role;
    }
}
