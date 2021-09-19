package com.gateway.app.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class SpringSecurityConfiguration {

    @Autowired
    private AuthenticationFilter authenticationFilter;

    @Bean
    public SecurityWebFilterChain configure(ServerHttpSecurity http){
        return http.authorizeExchange()
                    .pathMatchers("/api/security/oauth/**").permitAll()
                    .pathMatchers(HttpMethod.GET, "/api/products/list", "/api/items/list", "/api/users/user", "/api/items/detail/{id}/**", "/api/products/detail/{id}").permitAll()
                    .pathMatchers(HttpMethod.GET, "/api/users/user/{id}").hasAnyRole("ADMIN", "USER")
                    .pathMatchers("api/products/**", "api/items/**",  "/api/users/user/**").hasRole("ADMIN")
                    .anyExchange().authenticated()
                    .and().addFilterAt(authenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION).csrf().disable()
                    .build();
    }
    
}
