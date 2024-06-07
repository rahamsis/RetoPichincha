package com.example.reto.Repository;

import com.example.reto.domains.entity.CurrencyOperation;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyOperationRepository extends R2dbcRepository<CurrencyOperation, Long> {
}
