package com.ironhack.ironbank.service;


import com.ironhack.ironbank.repository.AdminRepository;
import com.ironhack.ironbank.DTO.AdminDTO;
import com.ironhack.ironbank.model.Admin;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    final AdminRepository adminRepo;

    public AdminService(AdminRepository adminRepo) {
        this.adminRepo = adminRepo;
    }

    //--------------------------------------------------------------
    // METHODS AND LOGIC:
    //--------------------------------------------------------------

    public AdminDTO add(Admin admin){
        return AdminDTO.fromEntity(adminRepo.save(admin));
    }

    public AdminDTO getByUsername(String username){
        return AdminDTO.fromEntity(adminRepo.findByUsername(username));
    }


}
