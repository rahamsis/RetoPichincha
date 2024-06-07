package com.example.reto.domains.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyExchangeDto {
    private Long id;
    private String from;
    private String to;
    private Double conversion;
}
