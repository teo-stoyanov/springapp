package com.example.springapp.dto;

import com.example.springapp.domain.Currency;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountTransactionRequest {

    @NotNull
    private Currency currency;

    @NotNull
    @DecimalMin(value = "5.00", message = "Minimum transaction amount is 5 EUR")
    private BigDecimal amount;
}