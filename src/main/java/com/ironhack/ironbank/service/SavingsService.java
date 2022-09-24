package com.ironhack.ironbank.service;

import com.ironhack.ironbank.helpclasses.Money;
import com.ironhack.ironbank.model.Savings;
import com.ironhack.ironbank.model.Transaction;
import com.ironhack.ironbank.repository.SavingsRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

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

    public Savings increaseBalance (UUID accountId, Money balanceToBeAdded, Transaction transaction){
        var account = savingsRepository.findById(accountId).get();
        Money newBalance = account.getBalance();
        newBalance.increaseAmount(balanceToBeAdded);
        account.setBalance(newBalance);
        //account.getTransactionList().add(transaction);
        return savingsRepository.save(account);
    }
    public Savings decreaseBalance (UUID accountId, Money balanceToBeRemove, Transaction transaction){
        var account = savingsRepository.findById(accountId).get();
        Money newBalance = account.getBalance();
        newBalance.decreaseAmount(balanceToBeRemove);
        account.setBalance(newBalance);
        //account.getTransactionList().add(transaction);
        return savingsRepository.save(account);
    }
}
