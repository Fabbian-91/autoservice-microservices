package com.autoservice.gateway_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

 //Configuracion de Spring Security para el Gateway.
 //Aqui se tiene que dejar pasar todas las peticiones para que el
 //JwtAuthenticationFilter sea el unico validando el JWT.

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                // Desactivar CSRF (API REST stateless, no usa cookies de sesion)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                // Desactivar el login por formulario (esto es una API)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)

                // Desactivar HTTP Basic (no se usa, se usa JWT)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)

                // Desactivar logout (no se usa)
                .logout(ServerHttpSecurity.LogoutSpec::disable)

                // Configurar CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Autorización: permitir todas las peticiones.
                // La validacion real del JWT la hace nuestro JwtAuthenticationFilter.
                .authorizeExchange(exchanges -> exchanges
                        .anyExchange().permitAll()
                )

                .build();
    }

     //Configuracion de CORS a nivel de Spring Security.
     //Debe estar alineada con la del application.yml.
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));  // Usa Patterns para permitir "*" con credentials
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization", "Content-Disposition"));
        config.setAllowCredentials(false);  // false porque usamos "*"
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
