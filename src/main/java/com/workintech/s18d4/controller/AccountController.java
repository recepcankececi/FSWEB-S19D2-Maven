package com.workintech.s18d4.controller;

import com.workintech.s18d4.dto.AccountResponse;
import com.workintech.s18d4.entity.Account;
import com.workintech.s18d4.entity.Customer;
import com.workintech.s18d4.service.AccountService;
import com.workintech.s18d4.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;
    private final CustomerService customerService;

    @Autowired
    public AccountController(AccountService accountService, CustomerService customerService) {
        this.accountService = accountService;
        this.customerService = customerService;
    }

    @GetMapping
    public List<AccountResponse> findAll() {
        return accountService.findAll().stream()
                .map(account -> new AccountResponse(account.getId(), account.getAccountName(), account.getMoneyAmount()))
                .toList();
    }

    @GetMapping("/{id}")
    public AccountResponse find(@PathVariable Long id) {
        Account account = accountService.find(id);
        return new AccountResponse(account.getId(), account.getAccountName(), account.getMoneyAmount());
    }

    @PostMapping("/{customerId}")
    public AccountResponse save(@PathVariable Long customerId, @RequestBody Account account) {
        Customer customer = customerService.find(customerId);
        account.setCustomer(customer);
        Account savedAccount = accountService.save(account);
        return new AccountResponse(savedAccount.getId(), savedAccount.getAccountName(), savedAccount.getMoneyAmount());
    }

    @PutMapping("/{customerId}")
    public AccountResponse update(@PathVariable Long customerId, @RequestBody Account account) {
        Customer customer = customerService.find(customerId);
        Account existingAccount = accountService.find(account.getId());
        existingAccount.setAccountName(account.getAccountName());
        existingAccount.setMoneyAmount(account.getMoneyAmount());
        existingAccount.setCustomer(customer);
        Account updatedAccount = accountService.save(existingAccount);
        return new AccountResponse(updatedAccount.getId(), updatedAccount.getAccountName(), updatedAccount.getMoneyAmount());
    }

    @DeleteMapping("/{id}")
    public AccountResponse delete(@PathVariable Long id) {
        Account account = accountService.find(id);
        accountService.delete(id);
        return new AccountResponse(account.getId(), account.getAccountName(), account.getMoneyAmount());
    }
}
