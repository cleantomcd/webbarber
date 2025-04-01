package com.webbarber.webbarber.infra.security;

import com.webbarber.webbarber.repository.BarberRepository;
import com.webbarber.webbarber.repository.UserRepository;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.List;

/**
 * Filtro de segurança responsável por interceptar as requisições HTTP para validar o token JWT
 * e autenticar o usuário no contexto de segurança do Spring.
 * Este filtro verifica a presença de um token JWT na requisição, valida o token, extrai a
 * função do usuário e, com base nisso, autentica o usuário e atribui as permissões apropriadas.
 */
@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BarberRepository barberRepository;

    /**
     * Método principal do filtro que intercepta as requisições HTTP. Verifica se a requisição
     * contém um token JWT válido, valida esse token e autentica o usuário com base nas informações extraídas.
     *
     * @param request a requisição HTTP
     * @param response a resposta HTTP
     * @param filterChain a cadeia de filtros da requisição
     * @throws ServletException se ocorrer um erro durante o processamento do filtro
     * @throws IOException se ocorrer um erro de entrada/saída durante o processamento do filtro
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var token = this.recoverToken(request);
        if (token != null) {

            var login = tokenService.validateToken(token);
            var role = tokenService.extractRole(token);
            UserDetails user;


            if ("ROLE_USER".equals(role)) {
                user = userRepository.findByLogin(login);
            } else if ("ROLE_ADMIN".equals(role)) {
                user = barberRepository.findByLogin(login);
            } else {
                throw new SecurityException("Role inválida no token");
            }


            var authorities = List.of(new SimpleGrantedAuthority(role));
            var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Recupera o token JWT da requisição HTTP.
     *
     * @param request a requisição HTTP
     * @return o token JWT, ou null se não houver token presente
     */
    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
