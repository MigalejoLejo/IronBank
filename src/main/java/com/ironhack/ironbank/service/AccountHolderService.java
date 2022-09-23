package com.ironhack.ironbank.service;

import com.ironhack.ironbank.DTO.AccountsDTO;
import com.ironhack.ironbank.model.*;
import com.ironhack.ironbank.repository.AccountHolderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AccountHolderService {

    final
    AccountHolderRepository accountHolderRepo;

    final CheckingService checkingService;
    final StudentCheckingService studentCheckingService;
    final CreditAccountService creditAccountService;
    final SavingsService savingsService;

    public AccountHolderService(AccountHolderRepository accountHolderRepo, CheckingService checkingService, StudentCheckingService studentCheckingService, CreditAccountService creditAccountService, SavingsService savingsService) {
        this.accountHolderRepo = accountHolderRepo;
        this.checkingService = checkingService;
        this.studentCheckingService = studentCheckingService;
        this.creditAccountService = creditAccountService;
        this.savingsService = savingsService;
    }


    public AccountHolder getByUsername(String username) {
        AccountHolder accountHolder = null;
        if (accountHolderRepo.findByUsername(username) != null){
            accountHolder = accountHolderRepo.findByUsername(username);
        }
        return accountHolder;
    }

    public AccountHolder getById(String id) {
        if (id == null || accountHolderRepo.findById(id).isEmpty()){
            return null;
        } else {
            return accountHolderRepo.findById(id).get();
        }
    }

    public AccountHolder add (AccountHolder accountHolder){
        return accountHolderRepo.save(accountHolder);
    }


    public AccountsDTO getAccounts(String id) {
        var user = accountHolderRepo.findById(id).orElseThrow();
         return AccountsDTO.fromAccountHolder(user);
    }

    public ResponseEntity<String> getAccountById(String ownerId, String accountId) {

        AccountsDTO lists = getAccounts(ownerId);
        Object account;

        if (lists.getPrimaryCheckings() != null) {
            for (Checking c : lists.getPrimaryCheckings()) {
                if (c.getAccountID().compareTo(UUID.fromString(accountId)) == 0) {
                    return ResponseEntity.ok("Primary Account found: \n" + c);
                }
            }
        }

        if (lists.getPrimaryStudentCheckings() != null) {
            for (StudentChecking c : lists.getPrimaryStudentCheckings()) {
                if (c.getAccountID().compareTo(UUID.fromString(accountId)) == 0) {
                    return ResponseEntity.ok("Primary Account found: \n" + c);
                }
            }
        }

        if (lists.getPrimaryCreditAccounts() != null) {
            for (CreditAccount c : lists.getPrimaryCreditAccounts()) {
                if (c.getAccountID().compareTo(UUID.fromString(accountId)) == 0) {
                    return ResponseEntity.ok("Primary Account found: \n" + c);
                }
            }
        }

        if (lists.getPrimarySavings() != null) {
            for (Savings c : lists.getPrimarySavings()) {
                if (c.getAccountID().compareTo(UUID.fromString(accountId)) == 0) {
                    return ResponseEntity.ok("Primary Account found: \n" + c);
                }
            }
        }

        if (lists.getSecondaryCheckings() != null) {
            for (Checking c : lists.getSecondaryCheckings()) {
                if (c.getAccountID().compareTo(UUID.fromString(accountId)) == 0) {
                    return ResponseEntity.ok("Secondary Account found: \n" + c);
                }
            }
        }

        if (lists.getSecondaryStudentCheckings() != null) {
            for (StudentChecking c : lists.getSecondaryStudentCheckings()) {
                if (c.getAccountID().compareTo(UUID.fromString(accountId)) == 0) {
                    return ResponseEntity.ok("Secondary Account found: \n" + c);
                }
            }
        }

        if (lists.getSecondaryCreditAccounts() != null) {
            for (CreditAccount c : lists.getSecondaryCreditAccounts()) {
                if (c.getAccountID().compareTo(UUID.fromString(accountId)) == 0) {
                    return ResponseEntity.ok("Secondary Account found: \n" + c);
                }
            }
        }

        if (lists.getSecondarySavings() != null) {
            for (Savings c : lists.getSecondarySavings()) {
                if (c.getAccountID().compareTo(UUID.fromString(accountId)) == 0) {
                    return ResponseEntity.ok("Secondary Account found: \n" + c);
                }
            }
        }

        return ResponseEntity.notFound().build();


    }



}
