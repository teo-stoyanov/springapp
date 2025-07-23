package com.example.springapp.controller;

import com.example.springapp.dto.AccountCreateRequest;
import com.example.springapp.dto.AccountResponse;
import com.example.springapp.dto.AccountTransactionRequest;
import com.example.springapp.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{userId}/accounts")
@RequiredArgsConstructor
@Tag(name = "Account Management", description = "Operations for managing user accounts and transactions")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN') or hasRole('VIEWER')")
    @Operation(summary = "Create a new account", description = "Creates a new account for the specified user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public AccountResponse createAccount(
            @Parameter(description = "User ID") @PathVariable Long userId,
            @Valid @RequestBody AccountCreateRequest request
    ) {
        return accountService.createAccount(userId, request);
    }

    @PostMapping("/{accountId}/deposit")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or hasRole('VIEWER')")
    @Operation(summary = "Deposit money", description = "Deposits money into the specified account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deposit successful"),
            @ApiResponse(responseCode = "400", description = "Invalid transaction data"),
            @ApiResponse(responseCode = "404", description = "User or account not found")
    })
    public AccountResponse deposit(
            @Parameter(description = "User ID") @PathVariable Long userId,
            @Parameter(description = "Account ID") @PathVariable Long accountId,
            @Valid @RequestBody AccountTransactionRequest request
    ) {
        return accountService.deposit(userId, accountId, request);
    }

    @PostMapping("/{accountId}/withdraw")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or hasRole('VIEWER')")
    @Operation(summary = "Withdraw money", description = "Withdraws money from the specified account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Withdrawal successful"),
            @ApiResponse(responseCode = "400", description = "Invalid transaction data or insufficient funds"),
            @ApiResponse(responseCode = "404", description = "User or account not found")
    })
    public AccountResponse withdraw(
            @Parameter(description = "User ID") @PathVariable Long userId,
            @Parameter(description = "Account ID") @PathVariable Long accountId,
            @Valid @RequestBody AccountTransactionRequest request
    ) {
        return accountService.withdraw(userId, accountId, request);
    }

    @GetMapping("/{accountId}/balance")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VIEWER')")
    @Operation(summary = "Get account balance", description = "Retrieves the current balance of the specified account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Balance retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User or account not found")
    })
    public AccountResponse getBalance(
            @Parameter(description = "User ID") @PathVariable Long userId,
            @Parameter(description = "Account ID") @PathVariable Long accountId
    ) {
        return accountService.getBalance(userId, accountId);
    }
}