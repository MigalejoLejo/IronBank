package com.ironhack.ironbank.service;


import com.ironhack.ironbank.repository.AdminRepository;
import com.ironhack.ironbank.users.model.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    final
    AdminRepository adminRepo;

    public AdminService(AdminRepository adminRepo) {
        this.adminRepo = adminRepo;
    }


    public Admin add(Admin admin){
        return adminRepo.save(admin);
    }
}
