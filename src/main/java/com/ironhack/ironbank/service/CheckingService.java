package com.ironhack.ironbank.service;


import com.ironhack.ironbank.model.Checking;
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

    public Checking findById (String id) {
        return checkingRepository.findById(UUID.fromString(id)).orElseThrow();
    }

    public Checking add(Checking account){
        return checkingRepository.save(account);
    }

}
