package com.ironhack.ironbank.service;


import com.ironhack.ironbank.helpclasses.Money;
import com.ironhack.ironbank.model.CreditAccount;
import com.ironhack.ironbank.model.Transaction;
import com.ironhack.ironbank.repository.CreditAccountRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreditAccountService {

    final
    CreditAccountRepository creditAccountRepository;

    public CreditAccountService(CreditAccountRepository creditAccountRepository) {
        this.creditAccountRepository = creditAccountRepository;
    }

    public CreditAccount findById (String id) {
        return creditAccountRepository.findById(UUID.fromString(id)).orElseThrow();
    }

    public CreditAccount add(CreditAccount account){
        return creditAccountRepository.save(account);
    }


    public boolean checkIfExists(UUID accountID) {
        return creditAccountRepository.findById(accountID).isPresent();
    }

    public CreditAccount increaseBalance (UUID accountId, Money balanceToBeAdded, Transaction transaction){
        var account = creditAccountRepository.findById(accountId).get();
        Money newBalance = account.getBalance();
        newBalance.increaseAmount(balanceToBeAdded);
        account.setBalance(newBalance);
        //account.getTransactionList().add(transaction);
        return creditAccountRepository.save(account);
    }

    public CreditAccount decreaseBalance (UUID accountId, Money balanceToBeRemove, Transaction transaction){
        var account = creditAccountRepository.findById(accountId).get();
        Money newBalance = account.getBalance();
        newBalance.decreaseAmount(balanceToBeRemove);
        account.setBalance(newBalance);
        //account.getTransactionList().add(transaction);
        return creditAccountRepository.save(account);
    }

}
