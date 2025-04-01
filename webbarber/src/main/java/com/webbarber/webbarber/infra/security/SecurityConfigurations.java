package com.webbarber.webbarber.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configurações de segurança para o sistema, incluindo autenticação, autorização e controle de sessões.
 * Esta classe define as configurações de segurança para a aplicação, como o gerenciamento de sessões, filtros de segurança
 * e regras de autorização.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    private final SecurityFilter securityFilter;

    /**
     * Construtor da classe SecurityConfigurations.
     *
     * @param securityFilter o filtro de segurança personalizado para interceptar requisições HTTP
     */
    public SecurityConfigurations(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    /**
     * Configura o filtro de segurança da aplicação, incluindo as regras de autorização de acesso
     * às diferentes rotas da aplicação.
     *
     * @param httpSecurity a configuração de segurança HTTP para o sistema
     * @return a instância configurada do filtro de segurança
     * @throws Exception se ocorrer um erro na configuração do filtro de segurança
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable) // Desativa a proteção CSRF
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Define que a aplicação será stateless
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll() // Permite acesso público ao endpoint de login
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll() // Permite acesso público ao endpoint de registro
                        .requestMatchers("/barber/**").hasRole("ADMIN") // Requer a role "ADMIN" para acessar rotas relacionadas a barbeiros
                        .anyRequest().authenticated()) // Requer autenticação para todas as outras requisições
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class) // Adiciona o filtro de segurança antes do filtro de autenticação padrão
                .build();
    }

    /**
     * Configura o gerenciador de autenticação para o sistema.
     *
     * @param authenticationConfiguration a configuração de autenticação do sistema
     * @return o gerenciador de autenticação configurado
     * @throws Exception se ocorrer um erro na configuração do gerenciador de autenticação
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configura o codificador de senha a ser utilizado pelo sistema para criptografar as senhas dos usuários.
     *
     * @return o codificador de senha (BCryptPasswordEncoder)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Utiliza o algoritmo BCrypt para criptografar as senhas
    }
}
