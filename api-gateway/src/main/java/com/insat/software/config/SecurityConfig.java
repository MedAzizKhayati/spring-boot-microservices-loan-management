package com.insat.software.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) {
        serverHttpSecurity
                .csrf().disable()
                .authorizeExchange(exchange ->
                        exchange.pathMatchers("/eureka/**")
                                .permitAll()
                                .anyExchange()
                                .authenticated())
                .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt)
                .addFilterAfter((exchange, chain) -> {
                    String sub = "";
                    // get sub from the JWT token
                    String token = exchange.getRequest().getHeaders().getFirst("Authorization");
                    // remove the "Bearer " prefix
                    token = token.substring(7);
                    // decode the JWT token
                    String[] chunks = token.split("\\.");
                    String payload = new String(java.util.Base64.getDecoder().decode(chunks[1]));
                    // parse the JSON payload
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        sub = mapper.readTree(payload).get("sub").asText();
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    String clientId = sub;
                    exchange = exchange.mutate()
                            .request(request -> {
                                request.headers(headers -> {
                                    headers.set("X-ClientId", clientId);
                                });
                            })
                            .build();
                    return chain.filter(exchange);
                }, SecurityWebFiltersOrder.AUTHORIZATION);
        return serverHttpSecurity.build();
    }
}
