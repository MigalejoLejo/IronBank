package com.ironhack.ironbank.service;


import com.ironhack.ironbank.DTO.TransactionDTO;
import com.ironhack.ironbank.helpclasses.Money;
import com.ironhack.ironbank.helpclasses.TypeOfAccount;
import com.ironhack.ironbank.model.*;
import com.ironhack.ironbank.repository.TransactionRepository;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Log
@Service
public class TransactionService {

    final
    TransactionRepository transactionRepository;
    final
    AccountHolderService accountHolderService ;
    final CheckingService checkingService;
    final StudentCheckingService studentCheckingService;
    final CreditAccountService creditAccountService;
    final SavingsService savingsService;

    public TransactionService(TransactionRepository transactionRepository, AccountHolderService accountHolderService, CheckingService checkingService, StudentCheckingService studentCheckingService, CreditAccountService creditAccountService, SavingsService savingsService) {
        this.transactionRepository = transactionRepository;
        this.accountHolderService = accountHolderService;
        this.checkingService = checkingService;
        this.studentCheckingService = studentCheckingService;
        this.creditAccountService = creditAccountService;
        this.savingsService = savingsService;
    }

    // --------------------------------------------------------------------------------------------------------

    public Transaction add (Transaction transaction){
        return transactionRepository.save(transaction);
    }

    // --------------------------------------------------------------------------------------------------------

    public Transaction findById(UUID id){
        return transactionRepository.findById(id).orElseThrow();
    }

    // --------------------------------------------------------------------------------------------------------

    public ResponseEntity<String> makeATransaction (TransactionDTO transactionDTO){

        Transaction logForOrigin = Transaction.createOutgoingTransactionFromDTO(transactionDTO);
        Transaction logForDestination = Transaction.createIncomingTransactionFromDTO(transactionDTO);

        TypeOfAccount originAccount = determineTypeOfAccount(UUID.fromString(transactionDTO.getOriginID()));
        TypeOfAccount destinationAccount = determineTypeOfAccount(UUID.fromString(transactionDTO.getDestinationID()));

        boolean transactionCheckForOrigin = false;
        boolean transactionCheckForDestination = false;

        switch (originAccount){
            case CHECKING -> {
                checkingService.decreaseBalance(
                        UUID.fromString(transactionDTO.getOriginID()),
                        new Money(new BigDecimal(transactionDTO.getAmount())),
                        logForOrigin);
                transactionCheckForOrigin = true;
            }
            case STUDENT_CHECKING -> {
                studentCheckingService.decreaseBalance(
                        UUID.fromString(transactionDTO.getOriginID()),
                        new Money(new BigDecimal(transactionDTO.getAmount())),
                        logForOrigin);
                transactionCheckForOrigin = true;
            }
            case CREDIT -> {
                creditAccountService.decreaseBalance(
                        UUID.fromString(transactionDTO.getOriginID()),
                        new Money(new BigDecimal(transactionDTO.getAmount())),
                        logForOrigin);
                transactionCheckForOrigin = true;
            }
            case SAVINGS -> {
                savingsService.decreaseBalance(
                        UUID.fromString(transactionDTO.getOriginID()),
                        new Money(new BigDecimal(transactionDTO.getAmount())),
                        logForOrigin);
                transactionCheckForOrigin = true;
            }
        }

        switch (destinationAccount){
            case CHECKING -> {
                checkingService.increaseBalance(
                        UUID.fromString(transactionDTO.getOriginID()),
                        new Money(new BigDecimal(transactionDTO.getAmount())),
                        logForDestination);
                transactionCheckForDestination = true;
            }
            case STUDENT_CHECKING -> {
                studentCheckingService.increaseBalance(
                        UUID.fromString(transactionDTO.getOriginID()),
                        new Money(new BigDecimal(transactionDTO.getAmount())),
                        logForDestination);
                transactionCheckForDestination = true;
            }
            case CREDIT -> {
                creditAccountService.increaseBalance(
                        UUID.fromString(transactionDTO.getOriginID()),
                        new Money(new BigDecimal(transactionDTO.getAmount())),
                        logForDestination);
                transactionCheckForDestination = true;
            }
            case SAVINGS -> {
                savingsService.increaseBalance(
                        UUID.fromString(transactionDTO.getOriginID()),
                        new Money(new BigDecimal(transactionDTO.getAmount())),
                        logForDestination);
                transactionCheckForDestination = true;
            }
        }

        if (transactionCheckForOrigin & transactionCheckForDestination){
            return ResponseEntity.ok("Transaction successful.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Something went wrong.");
        }
    }

    // --------------------------------------------------------------------------------------------------------

    public TypeOfAccount determineTypeOfAccount (UUID accountID){

        if (checkingService.checkIfExists(accountID)){
           return TypeOfAccount.CHECKING;
        }

        if (studentCheckingService.checkIfExists(accountID)){
          return TypeOfAccount.STUDENT_CHECKING;
        }

        if (creditAccountService.checkIfExists(accountID)){
            return TypeOfAccount.CREDIT;
        }

        if (savingsService.checkIfExists(accountID)){
            return TypeOfAccount.SAVINGS;
        }

        return TypeOfAccount.NON_EXISTENT;

    }

    public ResponseEntity<String> userMakeTransaction(String userId, TransactionDTO transactionDTO) {

        boolean accountBelongsToUser = checkIfAccountBelongsToUser(transactionDTO.getOriginID(), userId);

        if (accountBelongsToUser) {
            return makeATransaction(transactionDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("");
        }
    }

    public boolean checkIfAccountBelongsToUser(String accountId, String userId) {

        var accountsFromUser = accountHolderService.getAccounts(userId);

        if (accountsFromUser.getPrimaryCheckings() != null) {
            for (Checking account : accountsFromUser.getPrimaryCheckings()) {
                if (account.getAccountID().toString().equals(accountId)) {
                    return true;
                } else { log.severe("Not found in P Checkings");}
            }
        }

        if (accountsFromUser.getPrimaryStudentCheckings() != null) {
            for (StudentChecking account : accountsFromUser.getPrimaryStudentCheckings()) {
                if (account.getAccountID().toString().equals(accountId)) {
                    return true;
                } else { log.severe("Not found in P StudentCheckings");}
            }
        }

        if (accountsFromUser.getPrimaryCreditAccounts() != null) {
            for (CreditAccount account : accountsFromUser.getPrimaryCreditAccounts()) {
                if (account.getAccountID().toString().equals(accountId)) {
                    return true;
                } else { log.severe("Not found in p Credits");}
            }
        }

        if (accountsFromUser.getPrimarySavings() != null) {
            for (Savings account : accountsFromUser.getPrimarySavings()) {
                if (account.getAccountID().toString().equals(accountId)) {
                    return true;
                } else { log.severe("Not found in P Savings");}
            }
        }

        if (accountsFromUser.getSecondaryCheckings() != null) {
            for (Checking account : accountsFromUser.getSecondaryCheckings()) {
                if (account.getAccountID().toString().equals(accountId)) {
                    return true;
                } else { log.severe("Not found in P Checkings");}
            }
        }

        if (accountsFromUser.getSecondaryStudentCheckings() != null) {
            for (StudentChecking account : accountsFromUser.getSecondaryStudentCheckings()) {
                if (account.getAccountID().toString().equals(accountId)) {
                    return true;
                } else { log.severe("Not found in S StudentCheckings");}
            }
        }

        if (accountsFromUser.getSecondaryCreditAccounts() != null) {
            for (CreditAccount account : accountsFromUser.getSecondaryCreditAccounts()) {
                if (account.getAccountID().toString().equals(accountId)) {
                    return true;
                } else { log.severe("Not found in s Credits");}
            }
        }

        if (accountsFromUser.getSecondarySavings() != null) {
            for (Savings account : accountsFromUser.getSecondarySavings()) {
                if (account.getAccountID().toString().equals(accountId)) {
                    return true;
                } else { log.severe("Not found in S Savings");}

            }
        }
        return false;
    }

}
