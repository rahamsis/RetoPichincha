package com.example.reto.controllers;

import com.example.reto.handler.CurrencyHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class CurrencyController {

    private final CurrencyHandler currencyHandler;

    public CurrencyController(CurrencyHandler currencyHandler) {
        this.currencyHandler = currencyHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> currencyRoutes() {
        return RouterFunctions.route(POST("/currency/save"), currencyHandler::create)
                .andRoute(GET("/currency/all"), currencyHandler::all)
                .andRoute(PUT("/currency/update"), currencyHandler::update)
                .andRoute(POST("/currency-exchange"), currencyHandler::exchange)
                .andRoute(GET("/currency-exchange/history"), currencyHandler::showHistory);
    }
}
