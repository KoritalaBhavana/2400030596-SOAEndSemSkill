package com.library.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routes(
            RouteLocatorBuilder builder) {

        return builder.routes()

            .route("book-service", r -> r
                .path("/books/**")
                .uri("lb://BOOK-SERVICE"))

            .route("auth-service", r -> r
                .path("/auth/**")
                .uri("lb://AUTH-SERVICE"))

            .build();
    }
}