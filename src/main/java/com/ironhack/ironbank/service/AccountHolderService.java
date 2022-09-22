package com.ironhack.ironbank.service;

import com.ironhack.ironbank.DTO.AccountsDTO;
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


    public AccountHolder getByUsername(String username) {
        AccountHolder accountHolder = null;
        if (accountHolderRepo.findByUsername(username) != null){
            accountHolder = accountHolderRepo.findByUsername(username);
        }
        return accountHolder;
    }

    public AccountHolder getById(String id) {
        if (id == null || accountHolderRepo.findById(id).isEmpty()){
            return null;
        } else {
            return accountHolderRepo.findById(id).get();
        }
    }

    public AccountHolder add (AccountHolder accountHolder){
        return accountHolderRepo.save(accountHolder);
    }


    public AccountsDTO getAccounts(String id) {
        var user = accountHolderRepo.findById(id).orElseThrow();
         return AccountsDTO.fromAccountHolder(user);
    }
}
