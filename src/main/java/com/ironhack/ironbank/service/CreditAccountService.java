package com.ironhack.ironbank.service;


import com.ironhack.ironbank.DTO.TransactionDTO;
import com.ironhack.ironbank.helpclasses.Money;
import com.ironhack.ironbank.model.CreditAccount;
import com.ironhack.ironbank.repository.CreditAccountRepository;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Log
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

    public CreditAccount increaseBalance (TransactionDTO transaction){
        var account = creditAccountRepository.findById(UUID.fromString(transaction.getDestinationID())).orElseThrow();
        Money newBalance = account.getBalance();
        newBalance.increaseAmount(new BigDecimal(transaction.getAmount()));
        account.setBalance(newBalance);
        //account.getTransactionList().add(transaction);

        log.info("balance increased in origin account.");
        return creditAccountRepository.save(account);
    }

    public CreditAccount decreaseBalance (TransactionDTO transaction){
        var account = creditAccountRepository.findById(UUID.fromString(transaction.getOriginID())).orElseThrow();
        Money newBalance = account.getBalance();
        newBalance.decreaseAmount(new BigDecimal(transaction.getAmount()));
        account.setBalance(newBalance);
        //account.getTransactionList().add(transaction);

        log.info("balance decreased in origin account.");
        return creditAccountRepository.save(account);
    }

}
