package com.ironhack.ironbank.service;


import com.ironhack.ironbank.DTO.TransactionDTO;
import com.ironhack.ironbank.helpclasses.Money;
import com.ironhack.ironbank.model.Checking;
import com.ironhack.ironbank.model.Transaction;
import com.ironhack.ironbank.repository.CheckingRepository;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Log
@Service
public class CheckingService {

    final
    CheckingRepository checkingRepository;

    public CheckingService(CheckingRepository checkingRepository) {
        this.checkingRepository = checkingRepository;
    }

    public Checking findById (UUID id) {
        return checkingRepository.findById(id).orElseThrow();
    }

    public Boolean checkIfExists (UUID accountId){
        return checkingRepository.findById(accountId).isPresent();
    }

    public Checking increaseBalance (TransactionDTO transaction){
        var account = checkingRepository.findById(UUID.fromString(transaction.getDestinationID())).orElseThrow();
        Money newBalance = account.getBalance();
        newBalance.increaseAmount(new BigDecimal(transaction.getAmount()));
        account.setBalance(newBalance);
        //account.getTransactionList().add(transaction);

        log.info("balance increased in origin account.");
        return checkingRepository.save(account);
    }

    public Checking decreaseBalance (TransactionDTO transaction){
        var account = checkingRepository.findById(UUID.fromString(transaction.getOriginID())).orElseThrow();
        Money newBalance = account.getBalance();
        newBalance.decreaseAmount(new BigDecimal(transaction.getAmount()));
        account.setBalance(newBalance);
        //account.getTransactionList().add(transaction);

        log.info("balance decreased in origin account.");
        return checkingRepository.save(account);
    }

    public Checking add(Checking account){
        return checkingRepository.save(account);
    }

}
