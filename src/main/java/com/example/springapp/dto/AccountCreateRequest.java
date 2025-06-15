package com.example.springapp.dto;

import com.example.springapp.domain.Currency;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccountCreateRequest {
    @NotNull
    private Currency currency;
}