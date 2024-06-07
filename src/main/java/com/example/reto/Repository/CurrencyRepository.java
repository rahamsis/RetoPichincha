package com.example.reto.Repository;

import com.example.reto.domains.entity.CurrencyExchange;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CurrencyRepository extends R2dbcRepository<CurrencyExchange, Long> {
    Mono<CurrencyExchange> findByCurrencyFromAndCurrencyTo(String currencyFrom, String currencyTo);

    @Query("SELECT * FROM currency_exchange WHERE id <> :id and currency_from = :currencyFrom and currency_to = :currencyTo")
    Flux<CurrencyExchange> findByNotIdAndCurrency(@Param("id") Long id, @Param("currencyFrom") String currencyFrom,
                                       @Param("currencyTo") String currencyTo);

}
