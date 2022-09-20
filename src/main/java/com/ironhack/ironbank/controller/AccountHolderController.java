package com.ironhack.ironbank.controller;


import com.ironhack.ironbank.config.KeycloakProvider;
import com.ironhack.ironbank.service.AccountHolderService;
import com.ironhack.ironbank.DTO.AccountHolderDTO;
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

    public AccountHolderController(AccountHolderService accountHolderService, KeycloakProvider kcProvider) {
        this.accountHolderService = accountHolderService;
        this.kcProvider = kcProvider;
    }

    @GetMapping("/user")
    public ResponseEntity<AccountHolderDTO> getByUsername(Principal principal) {
        //todo: accounts should be visible too
        return ResponseEntity.ok(accountHolderService.getByUsername(principal.getName()));
    }



}
