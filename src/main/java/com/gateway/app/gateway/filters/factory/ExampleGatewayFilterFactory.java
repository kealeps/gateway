package com.gateway.app.gateway.filters.factory;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import lombok.Data;
import reactor.core.publisher.Mono;

@Component
public class ExampleGatewayFilterFactory extends AbstractGatewayFilterFactory<ExampleGatewayFilterFactory.Config>{
    
    private final Logger logger = LoggerFactory.getLogger(ExampleGatewayFilterFactory.class);

    public ExampleGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return new OrderedGatewayFilter((exchange, chain) ->{
            logger.info("ejecutando pre gateway filter factory: " + config.message);
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                Optional.ofNullable(config.cookieValor).ifPresent(response -> {
                     exchange.getResponse().addCookie(ResponseCookie.from(config.cookie, config.cookieValor).build());
                });
                logger.info("ejecutando post gateway filter factory: " + config.message);
            }));
        }, 2);
    }
    
    @Data
    public static class Config{
        private String message;
        private String cookieValor;
        private String cookie;
    }
}
