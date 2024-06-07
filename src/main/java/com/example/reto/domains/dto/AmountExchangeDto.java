package com.example.reto.domains.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AmountExchangeDto {
    private Long id;
    private String from;
    private String to;
    private Double amount;
}
