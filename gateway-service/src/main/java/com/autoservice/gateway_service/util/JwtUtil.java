package com.autoservice.gateway_service.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

/*
 * Utilidad para trabajar con tokens JWT.
 * Permite extraer información (usuario, roles) y validar el token.
 */
@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    //Extrae el rol del token soportando múltiples formatos
    public String extractRole(String token) {
        Claims claims = extractAllClaims(token);

        // 1. "rol" (string)
        Object rol = claims.get("rol");
        if (rol != null && !(rol instanceof List)) {
            return rol.toString();
        }

        // 2. "role" (string)
        Object role = claims.get("role");
        if (role != null && !(role instanceof List)) {
            return role.toString();
        }

        // 3. "roles" (lista) → tomar el primero
        Object roles = claims.get("roles");
        if (roles instanceof List<?> lista && !lista.isEmpty()) {
            return lista.get(0).toString();
        }

        // 4. "authorities" (lista) → tomar el primero
        Object authorities = claims.get("authorities");
        if (authorities instanceof List<?> lista && !lista.isEmpty()) {
            Object first = lista.get(0);
            // Si es un objeto Authority de Spring, usar su toString
            return first.toString();
        }

        log.warn("No se encontró claim de rol en el token");
        return null;
    }

    //Extrae el id del usuario soportando: "id" o "userId".
    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);

        Object id = claims.get("id");
        if (id == null) id = claims.get("userId");

        if (id == null) return null;

        try {
            return Long.valueOf(id.toString());
        } catch (NumberFormatException e) {
            log.warn("El claim 'id' no es numérico: {}", id);
            return null;
        }
    }

    //Extrae todos los roles como lista, soportando cualquier formato.
    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);

        Object roles = claims.get("roles");
        if (roles instanceof List) {
            return ((List<?>) roles).stream().map(Object::toString).toList();
        }

        Object authorities = claims.get("authorities");
        if (authorities instanceof List) {
            return ((List<?>) authorities).stream().map(Object::toString).toList();
        }

        String single = extractRole(token);
        return single != null ? List.of(single) : List.of();
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractAllClaims(token);
            Date expiration = claims.getExpiration();
            boolean notExpired = expiration != null && expiration.after(new Date());
            log.debug("Token válido: {}, expira en: {}", notExpired, expiration);
            return notExpired;
        } catch (Exception e) {
            log.warn("Token inválido: {}", e.getMessage());
            return false;
        }
    }
}