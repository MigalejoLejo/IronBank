package com.ironhack.ironbank.controller;


import com.ironhack.ironbank.DTO.AccountsDTO;
import com.ironhack.ironbank.DTO.TransactionDTO;
import com.ironhack.ironbank.config.KeycloakProvider;
import com.ironhack.ironbank.model.AccountHolder;
import com.ironhack.ironbank.service.AccountHolderService;
import com.ironhack.ironbank.service.TransactionService;
import lombok.extern.java.Log;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;


@RestController
@Log
@RequestMapping("/accountholder")
public class AccountHolderController {

    final
    AccountHolderService accountHolderService;
    final
    KeycloakProvider kcProvider;

    final TransactionService transactionService;

    public AccountHolderController(AccountHolderService accountHolderService, KeycloakProvider kcProvider, TransactionService transactionService) {
        this.accountHolderService = accountHolderService;
        this.kcProvider = kcProvider;
        this.transactionService = transactionService;
    }

    @GetMapping("/user")
    public ResponseEntity<AccountHolder> getByUsername(Principal principal) {
        return ResponseEntity.ok(accountHolderService.getByUsername(principal.getName()));
    }

    @GetMapping("/accounts")
    public ResponseEntity<AccountsDTO> getCheckings(Principal principal) {
        var userId = accountHolderService.getByUsername(principal.getName()).getId();
        return ResponseEntity.ok(accountHolderService.getAccounts(userId));
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<String> getAccountById(@PathVariable String accountId, Principal principal) {
        var userId = accountHolderService.getByUsername(principal.getName()).getId();
        //todo: accounts should be visible too
        return accountHolderService.getAccountById(userId, accountId);
    }

    @PostMapping("/make-a-transaction")
    public ResponseEntity<String> makeTransaction(@RequestBody TransactionDTO transactionDTO, Principal principal) {
        var userId = accountHolderService.getByUsername(principal.getName()).getId();
        return transactionService.userMakeTransaction(userId, transactionDTO);
    }


}
