package com.autoservice.usuarios.service;

import com.autoservice.usuarios.dto.LoginResponseDTO;
import com.autoservice.usuarios.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class JwtService {

    private final JwtEncoder encoder;
    private final long expiracion;

    public JwtService(
            JwtEncoder encoder,
            @Value("${security.jwt.expiration-seconds}") long expiracion
    ) {
        this.encoder = encoder;
        this.expiracion = expiracion;
    }

    public LoginResponseDTO generarToken(Usuario usuario) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("sistema-gestion-pedidos")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiracion))
                .subject(usuario.getCorreo())
                .claim("roles", List.of(usuario.getRol()))
                .claim("rol", usuario.getRol())
                .claim("usuarioId", usuario.getId())
                .claim("id", usuario.getId())
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256)
                .type("JWT")
                .build();
        String token = encoder.encode(JwtEncoderParameters.from(header, claims))
                .getTokenValue();

        return new LoginResponseDTO(
                token,
                "Bearer",
                expiracion,
                usuario.getCorreo(),
                usuario.getRol()
        );
    }
}
