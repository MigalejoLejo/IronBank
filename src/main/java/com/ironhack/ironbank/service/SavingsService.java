package com.ironhack.ironbank.service;

import com.ironhack.ironbank.model.Savings;
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




}
