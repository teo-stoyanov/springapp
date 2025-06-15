package com.example.springapp.service;

import com.example.springapp.domain.Account;
import com.example.springapp.domain.Currency;
import com.example.springapp.domain.User;
import com.example.springapp.dto.AccountCreateRequest;
import com.example.springapp.dto.AccountResponse;
import com.example.springapp.dto.AccountTransactionRequest;
import com.example.springapp.repository.AccountRepository;
import com.example.springapp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public AccountResponse createAccount(Long userId, AccountCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        boolean exists = user.getAccounts().stream()
                .anyMatch(acc -> acc.getCurrency() == request.getCurrency());

        if (exists) {
            throw new IllegalArgumentException("User already has an account in this currency");
        }

        Account account = new Account();
        account.setUser(user);
        account.setCurrency(request.getCurrency());
        account.setBalance(BigDecimal.ZERO);

        Account saved = accountRepository.save(account);

        AccountResponse response = new AccountResponse();
        response.setId(saved.getId());
        response.setCurrency(saved.getCurrency());
        response.setBalance(saved.getBalance());

        return response;
    }

    @Transactional
    public AccountResponse deposit(Long userId, Long accountId, AccountTransactionRequest request) {
        Account account = getVerifiedAccount(userId, accountId);

        BigDecimal amount = request.getAmount();
        Currency inputCurrency = request.getCurrency();
        Currency accountCurrency = account.getCurrency();

        if (amount.compareTo(new BigDecimal("5.00")) < 0) {
            throw new IllegalArgumentException("Minimum deposit is 5 EUR");
        }

        if (amount.compareTo(new BigDecimal("5000.00")) > 0) {
            throw new IllegalArgumentException("More than 5k should be in bank");
        }

        if (inputCurrency != accountCurrency) {
            // Apply auto conversion
            if (inputCurrency == Currency.LEVA && accountCurrency == Currency.EURO) {
                amount = amount.divide(new BigDecimal("2"), 2, RoundingMode.HALF_UP);
            } else if (inputCurrency == Currency.EURO && accountCurrency == Currency.LEVA) {
                amount = amount.multiply(new BigDecimal("2"));
            } else {
                throw new IllegalArgumentException("Invalid currency conversion");
            }
        }

        account.setBalance(account.getBalance().add(amount));
        Account updated = accountRepository.save(account);

        return mapToResponse(updated);
    }

    @Transactional
    public AccountResponse withdraw(Long userId, Long accountId, AccountTransactionRequest request) {
        Account account = getVerifiedAccount(userId, accountId);

        Currency inputCurrency = request.getCurrency();
        Currency accountCurrency = account.getCurrency();
        BigDecimal amount = request.getAmount();

        if (amount.compareTo(new BigDecimal("5.00")) < 0) {
            throw new IllegalArgumentException("Minimum withdrawal is 5 EUR");
        }

        // Auto conversion if needed
        if (inputCurrency != accountCurrency) {
            if (inputCurrency == Currency.LEVA && accountCurrency == Currency.EURO) {
                amount = amount.divide(new BigDecimal("2"), 2, RoundingMode.HALF_UP);
            } else if (inputCurrency == Currency.EURO && accountCurrency == Currency.LEVA) {
                amount = amount.multiply(new BigDecimal("2"));
            } else {
                throw new IllegalArgumentException("Invalid currency");
            }
        }

        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        account.setBalance(account.getBalance().subtract(amount));
        Account updated = accountRepository.save(account);

        return mapToResponse(updated);
    }

    @Transactional(readOnly = true)
    public AccountResponse getBalance(Long userId, Long accountId) {
        Account account = getVerifiedAccount(userId, accountId);
        return mapToResponse(account);
    }

    private Account getVerifiedAccount(Long userId, Long accountId) {
        return accountRepository.findById(accountId)
                .filter(acc -> acc.getUser().getId().equals(userId))
                .orElseThrow(() -> new EntityNotFoundException("Account not found or not linked to user"));
    }

    private AccountResponse mapToResponse(Account account) {
        AccountResponse response = new AccountResponse();
        response.setId(account.getId());
        response.setCurrency(account.getCurrency());
        response.setBalance(account.getBalance());
        return response;
    }
}