package com.ironhack.ironbank.service;


import com.ironhack.ironbank.DTO.AccountsDTO;
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
        log.info("logForOrigin created");

        Transaction logForDestination = Transaction.createIncomingTransactionFromDTO(transactionDTO);
        log.info("logForDestination created");


        log.info("type of origin account being determined:");
        TypeOfAccount originAccount = determineTypeOfAccount(transactionDTO.getOriginID());
        log.info("type of destination account being determined:");
        TypeOfAccount destinationAccount = determineTypeOfAccount(transactionDTO.getDestinationID());

        boolean transactionCheckForOrigin = false;
        boolean transactionCheckForDestination = false;

        switch (originAccount){
            case CHECKING -> {
                checkingService.decreaseBalance(transactionDTO);
                transactionCheckForOrigin = true;
            }
            case STUDENT_CHECKING -> {
                studentCheckingService.decreaseBalance(transactionDTO);
                transactionCheckForOrigin = true;
            }
            case CREDIT -> {
                creditAccountService.decreaseBalance(transactionDTO);
                transactionCheckForOrigin = true;
            }
            case SAVINGS -> {
                savingsService.decreaseBalance(transactionDTO);
                transactionCheckForOrigin = true;
            }
        }

        switch (destinationAccount){
            case CHECKING -> {
                checkingService.increaseBalance(transactionDTO);
                transactionCheckForDestination = true;
            }
            case STUDENT_CHECKING -> {
                studentCheckingService.increaseBalance(transactionDTO);
                transactionCheckForDestination = true;
            }
            case CREDIT -> {
                creditAccountService.increaseBalance(transactionDTO);
                transactionCheckForDestination = true;
            }
            case SAVINGS -> {
                savingsService.increaseBalance(transactionDTO);
                transactionCheckForDestination = true;
            }
        }

        if (transactionCheckForOrigin & transactionCheckForDestination){
            transactionRepository.save(logForOrigin);
            transactionRepository.save(logForDestination);
            return ResponseEntity.ok("Transaction successful.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Something went wrong.");
        }
    }

    // --------------------------------------------------------------------------------------------------------



    public ResponseEntity<String> userMakeTransaction(String userId, TransactionDTO transactionDTO) {

        boolean accountBelongsToUser = checkIfAccountBelongsToUser( userId, transactionDTO.getOriginID());

        if (accountBelongsToUser) {
            return makeATransaction(transactionDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("");
        }
    }

    public boolean checkIfAccountBelongsToUser(String userId, String accountId) {

        AccountsDTO userAccounts = accountHolderService.getAccounts(userId);

        if (userAccounts.getPrimaryCheckings() != null) {
            for (Checking c : userAccounts.getPrimaryCheckings()) {
                if (c.getAccountID().compareTo(UUID.fromString(accountId)) == 0) {
                    log.info("account found: \n"+c);
                    return true;
                }
            }
        }

        if (userAccounts.getPrimaryStudentCheckings() != null) {
            for (StudentChecking c : userAccounts.getPrimaryStudentCheckings()) {
                if (c.getAccountID().compareTo(UUID.fromString(accountId)) == 0) {
                    log.info("account found: \n"+c);
                    return true;
                }
            }
        }

        if (userAccounts.getPrimaryCreditAccounts() != null) {
            for (CreditAccount c : userAccounts.getPrimaryCreditAccounts()) {
                if (c.getAccountID().compareTo(UUID.fromString(accountId)) == 0) {
                    log.info("account found: \n"+c);
                    return true;
                }
            }
        }

        if (userAccounts.getPrimarySavings() != null) {
            for (Savings c : userAccounts.getPrimarySavings()) {
                if (c.getAccountID().compareTo(UUID.fromString(accountId)) == 0) {
                    log.info("account found: \n"+c);
                    return true;
                }
            }
        }

        if (userAccounts.getSecondaryCheckings() != null) {
            for (Checking c : userAccounts.getSecondaryCheckings()) {
                if (c.getAccountID().compareTo(UUID.fromString(accountId)) == 0) {
                    log.info("account found: \n"+c);
                    return true;
                }
            }
        }

        if (userAccounts.getSecondaryStudentCheckings() != null) {
            for (StudentChecking c : userAccounts.getSecondaryStudentCheckings()) {
                if (c.getAccountID().compareTo(UUID.fromString(accountId)) == 0) {
                    log.info("account found: \n"+c);
                    return true;
                }
            }
        }

        if (userAccounts.getSecondaryCreditAccounts() != null) {
            for (CreditAccount c : userAccounts.getSecondaryCreditAccounts()) {
                if (c.getAccountID().compareTo(UUID.fromString(accountId)) == 0) {
                    log.info("account found: \n"+c);
                    return true;
                }
            }
        }

        if (userAccounts.getSecondarySavings() != null) {
            for (Savings c : userAccounts.getSecondarySavings()) {
                if (c.getAccountID().compareTo(UUID.fromString(accountId)) == 0) {
                    log.info("account found: \n"+c);
                    return true;
                }
            }
        }

        return false;


    }

    public TypeOfAccount determineTypeOfAccount (String id){

        var accountID = UUID.fromString(id);

        if (checkingService.checkIfExists(accountID)){
            log.info("type of account determined: checking");
            return TypeOfAccount.CHECKING;
        }

        if (studentCheckingService.checkIfExists(accountID)){
            log.info("type of account determined: student checking");
            return TypeOfAccount.STUDENT_CHECKING;
        }

        if (creditAccountService.checkIfExists(accountID)){
            log.info("type of account determined: credit");
            return TypeOfAccount.CREDIT;
        }

        if (savingsService.checkIfExists(accountID)){
            log.info("type of account determined: savings");
            return TypeOfAccount.SAVINGS;
        }

        log.info("type of account determined: non_existent");
        return TypeOfAccount.NON_EXISTENT;

    }

}
