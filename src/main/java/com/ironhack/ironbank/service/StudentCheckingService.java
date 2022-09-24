package com.ironhack.ironbank.service;

import com.ironhack.ironbank.helpclasses.Money;
import com.ironhack.ironbank.model.StudentChecking;
import com.ironhack.ironbank.model.Transaction;
import com.ironhack.ironbank.repository.StudentCheckingRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

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

    public StudentChecking increaseBalance (UUID accountId, Money balanceToBeAdded, Transaction transaction){
        var account = studentCheckingRepository.findById(accountId).get();
        Money newBalance = account.getBalance();
        newBalance.increaseAmount(balanceToBeAdded);
        account.setBalance(newBalance);
        //account.getTransactionList().add(transaction);
        return studentCheckingRepository.save(account);
    }
    public StudentChecking decreaseBalance (UUID accountId, Money balanceToBeRemove, Transaction transaction){
        var account = studentCheckingRepository.findById(accountId).get();
        Money newBalance = account.getBalance();
        newBalance.decreaseAmount(balanceToBeRemove);
        account.setBalance(newBalance);
        //account.getTransactionList().add(transaction);
        return studentCheckingRepository.save(account);
    }
}
