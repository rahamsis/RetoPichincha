package com.example.reto.domains.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@ToString
@Table(name = "currency_operation")
public class CurrencyOperation {

    @Id
    private String id;
    @Column("user_id")
    @JsonIgnore
    private Long userId;
    @Column("username")
    private String username;
    @Column("currency_from")
    private String currencyFrom;
    @Column("currency_to")
    private String currencyTo;
    @Column("conversion")
    private Double conversion;
    @Column("amount")
    private Double amount;
    @Column("amount_converted")
    private Double amountConverted;


}
