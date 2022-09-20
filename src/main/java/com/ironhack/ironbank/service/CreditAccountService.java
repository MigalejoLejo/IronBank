package com.ironhack.ironbank.service;


import com.ironhack.ironbank.model.CreditAccount;
import com.ironhack.ironbank.repository.CreditAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreditAccountService {

    @Autowired
    CreditAccountRepository creditAccountRepository;

    public CreditAccount findById (String id) {
        return creditAccountRepository.findById(UUID.fromString(id)).orElseThrow();
    }

    public CreditAccount add(CreditAccount account){
        return creditAccountRepository.save(account);
    }


}
