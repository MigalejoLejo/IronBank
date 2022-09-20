package com.ironhack.ironbank.service;

import com.ironhack.ironbank.DTO.AccountHolderDTO;
import com.ironhack.ironbank.model.AccountHolder;
import com.ironhack.ironbank.repository.AccountHolderRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountHolderService {

    final
    AccountHolderRepository accountHolderRepo;

    public AccountHolderService(AccountHolderRepository accountHolderRepo) {
        this.accountHolderRepo = accountHolderRepo;
    }


    // METHODS AND LOGIC:
    // **************************************************************************
    public AccountHolderDTO getByUsername(String username) {
        AccountHolderDTO accountHolderDTO = null;
        if (accountHolderRepo.findByUsername(username) != null){
            accountHolderDTO = AccountHolderDTO.fromEntity(accountHolderRepo.findByUsername(username));
        }
        return accountHolderDTO;
    }

    public AccountHolderDTO getById(String id) {
        if (id == null || accountHolderRepo.findById(id).isEmpty()){
            return null;
        } else {
            return AccountHolderDTO.fromEntity(accountHolderRepo.findById(id).get());
        }
    }

    public AccountHolder add (AccountHolder accountHolder){
        return accountHolderRepo.save(accountHolder);
    }



}
