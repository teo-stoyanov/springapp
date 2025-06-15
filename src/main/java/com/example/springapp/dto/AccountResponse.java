package com.example.springapp.dto;

import com.example.springapp.domain.Currency;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountResponse {
    private Long id;
    private Currency currency;
    private BigDecimal balance;
}