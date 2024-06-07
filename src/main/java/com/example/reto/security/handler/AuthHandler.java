package com.example.reto.security.handler;

import com.example.reto.domains.entity.User;
import com.example.reto.security.dto.CreateUserDto;
import com.example.reto.security.dto.LoginDto;
import com.example.reto.security.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthHandler {
    private final UserService userService;

    public Mono<ServerResponse> login(ServerRequest request){

        Mono<LoginDto> dtoMono = request.bodyToMono(LoginDto.class);

        return dtoMono.flatMap(dto -> userService.login(dto)
                .flatMap(tokenDto -> {
                    Map<String, Object> responseMap = new HashMap<>();
                    responseMap.put("username", tokenDto.getUsername());
                    responseMap.put("roles", tokenDto.getRoles());
                    responseMap.put("token", tokenDto.getToken());

                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(responseMap);
                })
                .switchIfEmpty(ServerResponse.status(401).build())
        );
    }

    public Mono<ServerResponse> create(ServerRequest request){
        Mono<CreateUserDto> dtoMono = request.bodyToMono(CreateUserDto.class);

        return dtoMono.flatMap(dto -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.create(dto), User.class));
    }
}
