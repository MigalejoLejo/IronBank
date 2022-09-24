package com.ironhack.ironbank.service;

import com.ironhack.ironbank.DTO.TransactionDTO;
import com.ironhack.ironbank.helpclasses.Money;
import com.ironhack.ironbank.model.StudentChecking;
import com.ironhack.ironbank.repository.StudentCheckingRepository;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Log
@Service
public class StudentCheckingService {

    final
    StudentCheckingRepository studentCheckingRepository;

    public StudentCheckingService(StudentCheckingRepository studentCheckingRepository) {
        this.studentCheckingRepository = studentCheckingRepository;
    }

    public StudentChecking findById (String id) {
        return studentCheckingRepository.findById(UUID.fromString(id)).orElseThrow();
    }

    public StudentChecking add(StudentChecking account){
        return studentCheckingRepository.save(account);
    }

    public boolean checkIfExists(UUID accountID) {
        return studentCheckingRepository.findById(accountID).isPresent();
    }

    public StudentChecking increaseBalance (TransactionDTO transaction){
        var account = studentCheckingRepository.findById(UUID.fromString(transaction.getDestinationID())).orElseThrow();
        Money newBalance = account.getBalance();
        newBalance.increaseAmount(new BigDecimal(transaction.getAmount()));
        account.setBalance(newBalance);
        //account.getTransactionList().add(transaction);

        log.info("balance increased in origin account.");
        return studentCheckingRepository.save(account);
    }

    public StudentChecking decreaseBalance (TransactionDTO transaction){
        var account = studentCheckingRepository.findById(UUID.fromString(transaction.getOriginID())).orElseThrow();
        Money newBalance = account.getBalance();
        newBalance.decreaseAmount(new BigDecimal(transaction.getAmount()));
        account.setBalance(newBalance);
        //account.getTransactionList().add(transaction);

        log.info("balance decreased in origin account.");
        return studentCheckingRepository.save(account);
    }
}
