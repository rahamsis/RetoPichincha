package com.example.reto.handler;

import com.example.reto.domains.dto.CurrencyExchangeDto;
import com.example.reto.domains.dto.ExchangeDto;
import com.example.reto.exception.CustomErrorResponse;
import com.example.reto.domains.entity.CurrencyExchange;
import com.example.reto.domains.entity.CurrencyOperation;
import com.example.reto.security.jwt.JwtProvider;
import com.example.reto.services.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class CurrencyHandler {

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private JwtProvider jwtProvider;

    public Mono<ServerResponse> all(ServerRequest request) {
        Flux<CurrencyExchangeDto> currencyExchangeDtoFlux = currencyService.findAll()
                .map(currencyExchange -> CurrencyExchangeDto
                        .builder()
                        .id(currencyExchange.getId())
                        .from(currencyExchange.getCurrencyFrom())
                        .to(currencyExchange.getCurrencyTo())
                        .conversion(currencyExchange.getConversion())
                        .build());

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(currencyExchangeDtoFlux, CurrencyExchangeDto.class);
    }

    public Mono<ServerResponse> showHistory(ServerRequest request) {
        Flux<CurrencyOperation> currencyOperationFlux = currencyService.history();

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(currencyOperationFlux, CurrencyOperation.class);
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        Mono<CurrencyExchangeDto> currencyExchangeDtoMono = request.bodyToMono(CurrencyExchangeDto.class);
        return currencyExchangeDtoMono
                .map(dto -> CurrencyExchange
                        .builder()
                        .currencyFrom(dto.getFrom())
                        .currencyTo(dto.getTo())
                        .conversion(dto.getConversion())
                        .build())
                .flatMap(dto -> currencyService.store(dto))
                .flatMap(currencyExchange -> {
                    CurrencyExchangeDto responseDto = CurrencyExchangeDto
                            .builder()
                            .id(currencyExchange.getId())
                            .from(currencyExchange.getCurrencyFrom())
                            .to(currencyExchange.getCurrencyTo())
                            .conversion(currencyExchange.getConversion())
                            .build();
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(responseDto);
                })
                .onErrorResume(throwable -> {
                    CustomErrorResponse errorResponse = new CustomErrorResponse();
                    errorResponse.setStatus(500);
                    errorResponse.setMessage(throwable.getMessage());
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(errorResponse);
                });
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        Mono<CurrencyExchangeDto> currencyExchangeDtoMono = request.bodyToMono(CurrencyExchangeDto.class);
        return currencyExchangeDtoMono.map(dto -> CurrencyExchange
                        .builder()
                        .id(dto.getId())
                        .currencyFrom(dto.getFrom())
                        .currencyTo(dto.getTo())
                        .conversion(dto.getConversion())
                        .build())
                .flatMap(currencyExchange -> currencyService.update(Mono.just(currencyExchange)))
                .flatMap(currencyExchange -> {
                    CurrencyExchangeDto responseDto = CurrencyExchangeDto
                            .builder()
                            .id(currencyExchange.getId())
                            .from(currencyExchange.getCurrencyFrom())
                            .to(currencyExchange.getCurrencyTo())
                            .conversion(currencyExchange.getConversion())
                            .build();
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(responseDto);
                }).onErrorResume(throwable -> {
                    CustomErrorResponse errorResponse = new CustomErrorResponse();
                    errorResponse.setStatus(500);
                    errorResponse.setMessage(throwable.getMessage());
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(errorResponse);
                });
    }

    public Mono<ServerResponse> exchange(ServerRequest request) {
        String token = request.headers().firstHeader("Authorization").replace("Bearer ", "");
        String userId = jwtProvider.getClaims(token).get("id").toString();
        String username = jwtProvider.getSubject(token);
        Mono<ExchangeDto> exchangeDtoMono = request.bodyToMono(ExchangeDto.class);
        return exchangeDtoMono
                .flatMap(dto -> {
                    Mono<CurrencyExchange> mono = currencyService.findCurrencyData(dto);
                    return mono.flatMap(currencyExchange -> {

                        double converted = dto.getAmount() * currencyExchange.getConversion();
                        CurrencyOperation operation = new CurrencyOperation();
                        operation.setCurrencyFrom(dto.getFrom());
                        operation.setCurrencyTo(dto.getTo());
                        operation.setAmount(dto.getAmount());
                        operation.setConversion(currencyExchange.getConversion());
                        operation.setAmountConverted(converted);
                        operation.setUserId(Long.valueOf(userId));
                        operation.setUsername(username);

                        return currencyService.saveOperation(operation);
                    });
                })
                .flatMap(operation -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(operation))
                .onErrorResume(throwable -> {
                    CustomErrorResponse errorResponse = new CustomErrorResponse();
                    errorResponse.setStatus(500);
                    errorResponse.setMessage(throwable.getMessage());
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(errorResponse);
                });
    }
}
