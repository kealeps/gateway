package com.gateway.app.gateway.filters;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
//import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class GlobalFilterExample implements GlobalFilter, Ordered{

    private final Logger logger = LoggerFactory.getLogger(GlobalFilterExample.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info("ejecutando pre filtro");
        exchange.getRequest().mutate().headers(header -> {
            header.add("token", "123456");
        });
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
             logger.info("ejecutando post filtro");
             Optional.ofNullable(exchange.getRequest().getHeaders().getFirst("token")).ifPresent( 
                 response -> {
                    exchange.getResponse().getHeaders().add("token", response);
                 }
             );
             exchange.getResponse().getCookies().add("color", ResponseCookie.from("color", "rojo").build());
            // exchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);
        }));
    }

    @Override
    public int getOrder() {
        return 1;
    }
    
}
