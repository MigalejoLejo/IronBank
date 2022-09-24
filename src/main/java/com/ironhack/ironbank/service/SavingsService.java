package com.ironhack.ironbank.service;

import com.ironhack.ironbank.DTO.TransactionDTO;
import com.ironhack.ironbank.helpclasses.Money;
import com.ironhack.ironbank.model.Savings;

import com.ironhack.ironbank.repository.SavingsRepository;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Log
@Service
public class SavingsService {

    final
    SavingsRepository savingsRepository;

    public SavingsService(SavingsRepository savingsRepository) {
        this.savingsRepository = savingsRepository;
    }

    public Savings findById (String id){
       return savingsRepository.findById(UUID.fromString(id)).orElseThrow();
    }

    public Savings add(Savings account){
        return savingsRepository.save(account);
    }


    public boolean checkIfExists(UUID accountID) {
        return savingsRepository.findById(accountID).isPresent();
    }

    public Savings increaseBalance (TransactionDTO transaction){
        var account = savingsRepository.findById(UUID.fromString(transaction.getDestinationID())).orElseThrow();
        Money newBalance = account.getBalance();
        newBalance.increaseAmount(new BigDecimal(transaction.getAmount()));
        account.setBalance(newBalance);
        //account.getTransactionList().add(transaction);

        log.info("balance increased in origin account.");
        return savingsRepository.save(account);
    }

    public Savings decreaseBalance (TransactionDTO transaction){
        var account = savingsRepository.findById(UUID.fromString(transaction.getOriginID())).orElseThrow();
        Money newBalance = account.getBalance();
        newBalance.decreaseAmount(new BigDecimal(transaction.getAmount()));
        account.setBalance(newBalance);
        //account.getTransactionList().add(transaction);

        log.info("balance decreased in origin account.");
        return savingsRepository.save(account);
    }
}
