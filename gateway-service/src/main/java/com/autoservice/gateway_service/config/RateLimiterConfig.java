package com.autoservice.gateway_service.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

/*Configuración del Rate Limiter del Gateway.
 *
 * Define como se identifica a cada consumidor para aplicar el limite.
 * Estrategia: por usuario autenticado (email del JWT), con fallback a IP.
 *
 *  Requisito: Aplicar rate limiting basico para evitar que un usuario sature el sistema.
 */
@Configuration
public class RateLimiterConfig {

    /*KeyResolver principal: identifica al usuario por su email del JWT.
     * El email llega en el header X-User-Email (propagado por JwtAuthenticationFilter).
     * Si no hay email se usa la IP del cliente.*/
    @Bean
    @Primary
    public KeyResolver userKeyResolver() {
        return exchange -> {
            // 1. Intentar identificar por usuario autenticado
            String userEmail = exchange.getRequest().getHeaders().getFirst("X-User-Email");
            if (userEmail != null && !userEmail.isEmpty()) {
                return Mono.just("user:" + userEmail);
            }

            // 2. Fallback: por IP (para rutas publicas como /api/auth/login)
            String ip = getClientIp(exchange);
            return Mono.just("ip:" + ip);
        };
    }

    /*KeyResolver secundario: siempre usa la IP.
     * Para las rutas sensibles (como: /api/auth/login contra fuerza bruta).
     */
    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.just("ip:" + getClientIp(exchange));
    }

    //Extrae la IP real del cliente considerando proxies.
    private String getClientIp(org.springframework.web.server.ServerWebExchange exchange) {
        String xForwardedFor = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        String xRealIp = exchange.getRequest().getHeaders().getFirst("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        if (exchange.getRequest().getRemoteAddress() != null) {
            return exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
        }
        return "desconocida";
    }
}
