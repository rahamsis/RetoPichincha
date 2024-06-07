package com.example.reto.security.services;

import com.example.reto.domains.entity.User;
import com.example.reto.exception.CustomException;
import com.example.reto.security.dto.CreateUserDto;
import com.example.reto.security.dto.LoginDto;
import com.example.reto.security.dto.UserDetailsWithToken;
import com.example.reto.security.enums.Role;
import com.example.reto.security.jwt.JwtProvider;
import com.example.reto.security.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    public Mono<UserDetailsWithToken> login(LoginDto loginDto) {

        return userRepository.findByUsername(loginDto.getUsername())
            .filter(user -> passwordEncoder.matches(loginDto.getPassword(), user.getPassword()))
            .flatMap(user -> {
                String token = jwtProvider.generateToken(user, new HashMap<>() {{
                    put("id", user.getId());
                }});
                UserDetailsWithToken userDetailsWithToken = new UserDetailsWithToken(user.getUsername(), user.getAuthorities(), token);
                return Mono.just(userDetailsWithToken);
            })
            .switchIfEmpty(Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "bad credentials")));
    }

    public Mono<User> create(CreateUserDto createUserDto) {
        User user = User.builder()
                .username(createUserDto.getUsername())
                .password(passwordEncoder.encode(createUserDto.getPassword()))
                .roles(Role.ROLE_ADMIN.name() + ", " + Role.ROLE_USER.name())
                .build();

        Mono<Boolean> userExists = userRepository.findByUsername(createUserDto.getUsername()).hasElement();
        return userExists.flatMap(myBool -> myBool
                ? Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "user already exists"))
                : userRepository.save(user));
    }

}
