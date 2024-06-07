package com.example.reto.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDetailsWithToken {
    private String username;
    private Collection<? extends GrantedAuthority> roles;
    private String token;
}
