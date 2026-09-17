package com.autoservice.gateway_service.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.ratelimit.AbstractRateLimiter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*Rate Limiter en memoria.
 *
 * Usa un algoritmo de "token bucket" simplificado:
 * - Cada usuario tiene un "cubo" con N tokens disponibles.
 * - Cada petición consume 1 token.
 * - Los tokens se recargan a razón de X por segundo.
 * - Si no hay tokens entonces se devuelve 429 Too Many Requests.
 */
@Slf4j
@Component
public class InMemoryRateLimiter extends AbstractRateLimiter<InMemoryRateLimiter.Config> {

    public static final String CONFIGURATION_PROPERTY_NAME = "in-memory-rate-limiter";

    private final Map<String, TokenBucket> buckets = new ConcurrentHashMap<>();

    public InMemoryRateLimiter() {
        super(Config.class, CONFIGURATION_PROPERTY_NAME, null);
    }

    @Override
    public Mono<Response> isAllowed(String routeId, String id) {
        //Configuración por defecto: 10 tokens/seg, capacidad maxima 20
        int replenishRate = 10;
        int burstCapacity = 20;

        TokenBucket bucket = buckets.computeIfAbsent(id, k -> new TokenBucket(burstCapacity, replenishRate));

        boolean allowed = bucket.tryConsume(1);

        if (allowed) {
            log.debug("Rate limit OK para '{}' ({} tokens restantes)", id, bucket.getTokens());
            return Mono.just(new Response(true, Map.of("remaining", String.valueOf(bucket.getTokens()))));
        } else {
            log.warn("Rate limit EXCEDIDO para '{}'", id);
            return Mono.just(new Response(false, Map.of("remaining", "0")));
        }
    }

    //Cubo de tokens (Token Bucket)
    private static class TokenBucket {
        private final int capacity;
        private final int replenishRate; // tokens por segundo
        private double tokens;
        private Instant lastRefill;

        public TokenBucket(int capacity, int replenishRate) {
            this.capacity = capacity;
            this.replenishRate = replenishRate;
            this.tokens = capacity;
            this.lastRefill = Instant.now();
        }

        public synchronized boolean tryConsume(int amount) {
            refill();
            if (tokens >= amount) {
                tokens -= amount;
                return true;
            }
            return false;
        }

        private void refill() {
            Instant now = Instant.now();
            long elapsedMs = now.toEpochMilli() - lastRefill.toEpochMilli();
            double tokensToAdd = (elapsedMs / 1000.0) * replenishRate;
            tokens = Math.min(capacity, tokens + tokensToAdd);
            lastRefill = now;
        }

        public int getTokens() {
            return (int) tokens;
        }
    }

    //Configuración del rate limiter
    public static class Config {
        private int replenishRate = 10;
        private int burstCapacity = 20;
        private int requestedTokens = 1;

        public int getReplenishRate() { return replenishRate; }
        public void setReplenishRate(int replenishRate) { this.replenishRate = replenishRate; }

        public int getBurstCapacity() { return burstCapacity; }
        public void setBurstCapacity(int burstCapacity) { this.burstCapacity = burstCapacity; }

        public int getRequestedTokens() { return requestedTokens; }
        public void setRequestedTokens(int requestedTokens) { this.requestedTokens = requestedTokens; }
    }
}
