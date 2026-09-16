package com.autoservice.usuarios.service;

import com.autoservice.usuarios.dto.LoginResponse;
import com.autoservice.usuarios.entity.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;

@Service
public class JwtService {
  private final JwtEncoder encoder; private final long expiration;
  public JwtService(JwtEncoder encoder,@Value("${security.jwt.expiration-seconds}") long expiration){this.encoder=encoder;this.expiration=expiration;}
  public LoginResponse issue(Usuario u){
    Instant now=Instant.now();
    JwtClaimsSet claims=JwtClaimsSet.builder().issuer("sistema-gestion-pedidos").issuedAt(now).expiresAt(now.plusSeconds(expiration)).subject(u.getCorreo()).claim("roles",List.of(u.getRol())).claim("rol",u.getRol()).claim("usuarioId",u.getId()).claim("id",u.getId()).build();
    JwsHeader header=JwsHeader.with(MacAlgorithm.HS256).type("JWT").build();
    String token=encoder.encode(JwtEncoderParameters.from(header,claims)).getTokenValue();
    return new LoginResponse(token,"Bearer",expiration,u.getCorreo(),u.getRol());
  }
}
