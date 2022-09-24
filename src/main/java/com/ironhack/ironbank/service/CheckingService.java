package com.ironhack.ironbank.service;


import com.ironhack.ironbank.helpclasses.Money;
import com.ironhack.ironbank.model.Checking;
import com.ironhack.ironbank.model.Transaction;
import com.ironhack.ironbank.repository.CheckingRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

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

    public Checking increaseBalance (UUID accountId, Money balanceToBeAdded, Transaction transaction){
        var account = checkingRepository.findById(accountId).get();
        Money newBalance = account.getBalance();
        newBalance.increaseAmount(balanceToBeAdded);
        account.setBalance(newBalance);
        //account.getTransactionList().add(transaction);
        return checkingRepository.save(account);
    }

    public Checking decreaseBalance (UUID accountId, Money balanceToBeRemove, Transaction transaction){
        var account = checkingRepository.findById(accountId).get();
        Money newBalance = account.getBalance();
        newBalance.decreaseAmount(balanceToBeRemove);
        account.setBalance(newBalance);
        //account.getTransactionList().add(transaction);
        return checkingRepository.save(account);
    }

    public Checking add(Checking account){
        return checkingRepository.save(account);
    }

}
