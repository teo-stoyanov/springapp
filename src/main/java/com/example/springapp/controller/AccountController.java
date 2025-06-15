package com.example.springapp.controller;

import com.example.springapp.dto.AccountCreateRequest;
import com.example.springapp.dto.AccountResponse;
import com.example.springapp.dto.AccountTransactionRequest;
import com.example.springapp.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{userId}/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponse createAccount(
            @PathVariable Long userId,
            @Valid @RequestBody AccountCreateRequest request
    ) {
        return accountService.createAccount(userId, request);
    }

    @PostMapping("/{accountId}/deposit")
    @ResponseStatus(HttpStatus.OK)
    public AccountResponse deposit(
            @PathVariable Long userId,
            @PathVariable Long accountId,
            @Valid @RequestBody AccountTransactionRequest request
    ) {
        return accountService.deposit(userId, accountId, request);
    }

    @PostMapping("/{accountId}/withdraw")
    @ResponseStatus(HttpStatus.OK)
    public AccountResponse withdraw(
            @PathVariable Long userId,
            @PathVariable Long accountId,
            @Valid @RequestBody AccountTransactionRequest request
    ) {
        return accountService.withdraw(userId, accountId, request);
    }

    @GetMapping("/{accountId}/balance")
    public AccountResponse getBalance(
            @PathVariable Long userId,
            @PathVariable Long accountId
    ) {
        return accountService.getBalance(userId, accountId);
    }
}