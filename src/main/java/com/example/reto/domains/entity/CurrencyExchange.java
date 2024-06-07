package com.example.reto.domains.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@ToString
@Table(name = "currency_exchange")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyExchange {

    @Id
    private long id;
    @Column("currency_from")
    private String currencyFrom;
    @Column("currency_to")
    private String currencyTo;
    @Column("conversion")
    private Double conversion;

}
