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

//Utilidad para trabajar con tokens JWT.
//Permite extraer información (usuario, roles) y validar el token.
@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    //Construye la clave de firma a partir del secreto.
    //Debe tener al menos 32 caracteres para HS256.
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    //Extrae todos los claims del token.
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    //Extrae el username (subject) del token.
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    //Extrae el rol del token.
    // Puede venir como "rol" o "role".
    public String extractRole(String token) {
        Object rol = extractAllClaims(token).get("rol");
        return rol != null ? rol.toString() : null;

//        Claims claims = extractAllClaims(token);
//        Object rol = claims.get("rol");
//        if (rol == null) rol = claims.get("role");
//        return rol != null ? rol.toString() : null;
    }

    //Id del usuario (util para reenviar a otros servicios)
    public Long extractUserId(String token) {
        Object id = extractAllClaims(token).get("id");
        return id != null ? Long.valueOf(id.toString()) : null;
    }

    //Extrae la lista de roles si el token los tiene como array.
    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        Object roles = claims.get("roles");
        if (roles instanceof List) {
            return (List<String>) roles;
        }
        return List.of();
    }

    // Valida si el token es valido (firma correcta + no expirado).
    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractAllClaims(token);
            Date expiration = claims.getExpiration();
            boolean notExpired = expiration != null && expiration.after(new Date());
            log.debug("Token valido: {}, expira en: {}", notExpired, expiration);
            return notExpired;
        } catch (Exception e) {
            log.warn("Token invalido: {}", e.getMessage());
            return false;
        }
    }
}