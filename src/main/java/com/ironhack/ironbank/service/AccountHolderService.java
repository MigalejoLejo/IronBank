package com.ironhack.ironbank.service;

import com.ironhack.ironbank.DTO.AccountHolderDTO;
import com.ironhack.ironbank.model.AccountHolder;
import com.ironhack.ironbank.repository.AccountHolderRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountHolderService {

    final
    AccountHolderRepository acHoRepo;

    public AccountHolderService(AccountHolderRepository acHoRepo) {
        this.acHoRepo = acHoRepo;
    }



    //--------------------------------------------------------------
    // METHODS AND LOGIC:
    //--------------------------------------------------------------

    public AccountHolderDTO add(AccountHolder accountHolder){
        return AccountHolderDTO.fromEntity(acHoRepo.save(accountHolder));
    }

    public AccountHolderDTO getByUsername(String username){
        var accHolder = acHoRepo.findByUsername(username);
        return AccountHolderDTO.fromEntity(accHolder);
    }



}
