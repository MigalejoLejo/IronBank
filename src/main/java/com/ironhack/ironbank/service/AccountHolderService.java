package com.ironhack.ironbank.service;


import com.ironhack.ironbank.users.model.AccountHolder;
import com.ironhack.ironbank.repository.AccountHolderRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountHolderService {

    final
    AccountHolderRepository acHoRepo;

    public AccountHolderService(AccountHolderRepository acHoRepo) {
        this.acHoRepo = acHoRepo;
    }


    public AccountHolder add(AccountHolder accountHolder){
        return acHoRepo.save(accountHolder);
    }

    public AccountHolder get(String id){
        return acHoRepo.findById(id).orElseThrow();
    }
}
