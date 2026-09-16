package com.autoservice.usuarios.config;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.*;
import com.nimbusds.jose.jwk.source.*;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.*;
import org.springframework.security.web.SecurityFilterChain;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

@Configuration
public class SecurityConfig {
  @Bean PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
  @Bean SecretKey jwtSecretKey(@Value("${security.jwt.secret}") String value){byte[] b;try{b=Base64.getDecoder().decode(value);}catch(IllegalArgumentException ex){b=value.getBytes(StandardCharsets.UTF_8);}if(b.length<32)throw new IllegalArgumentException("JWT_SECRET debe contener al menos 32 bytes");return new SecretKeySpec(b,"HmacSHA256");}
  @Bean JwtEncoder jwtEncoder(SecretKey key){OctetSequenceKey jwk=new OctetSequenceKey.Builder(key).algorithm(JWSAlgorithm.HS256).build();JWKSource<SecurityContext> source=new ImmutableJWKSet<>(new JWKSet(jwk));return new NimbusJwtEncoder(source);}
  @Bean JwtDecoder jwtDecoder(SecretKey key){NimbusJwtDecoder d=NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS256).build();d.setJwtValidator(JwtValidators.createDefaultWithIssuer("sistema-gestion-pedidos"));return d;}
  @Bean JwtAuthenticationConverter jwtAuthenticationConverter(){JwtGrantedAuthoritiesConverter a=new JwtGrantedAuthoritiesConverter();a.setAuthoritiesClaimName("roles");a.setAuthorityPrefix("ROLE_");JwtAuthenticationConverter c=new JwtAuthenticationConverter();c.setJwtGrantedAuthoritiesConverter(a);return c;}
  @Bean SecurityFilterChain securityFilterChain(HttpSecurity http,JwtAuthenticationConverter converter)throws Exception{return http.csrf(c->c.disable()).sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authorizeHttpRequests(a->a.requestMatchers(HttpMethod.POST,"/api/usuarios/login","/api/auth/login","/api/auth/register").permitAll().requestMatchers(HttpMethod.POST,"/api/usuarios/validar-token").permitAll().requestMatchers("/actuator/health","/actuator/info").permitAll().anyRequest().hasRole("ADMINISTRADOR")).oauth2ResourceServer(o->o.jwt(j->j.jwtAuthenticationConverter(converter))).build();}
}
