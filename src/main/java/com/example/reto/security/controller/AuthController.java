package com.example.reto.security.controller;

import com.example.reto.security.handler.AuthHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

@Configuration
@Slf4j
public class AuthController {

    private final AuthHandler authHandler;

    public AuthController(AuthHandler authHandler){
        this.authHandler = authHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> authRoutes() {
        return RouterFunctions.route(POST("auth/login"), authHandler::login)
                .andRoute(POST("auth/create"), authHandler::create);
    }
}
