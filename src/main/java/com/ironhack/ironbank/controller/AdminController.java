package com.ironhack.ironbank.controller;


import com.ironhack.ironbank.DTO.CheckingDTO;
import com.ironhack.ironbank.DTO.AccountHolderDTO;
import com.ironhack.ironbank.DTO.AdminDTO;
import com.ironhack.ironbank.model.AccountHolder;
import com.ironhack.ironbank.service.AdminService;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.Response;

@Log
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService kcAdminClient;
    private final AdminService adminService;

    public AdminController(AdminService kcAdminClient,  AdminService adminService) {
        this.kcAdminClient = kcAdminClient;
        this.adminService = adminService;
    }



    //**************************************************************
    //            -------       USERS       -------
    //**************************************************************

    // CREATE
    // **************************************************************************
    @PostMapping(value = "/create/accountholder")
    public ResponseEntity<?> createKeycloakUser(@RequestBody AccountHolderDTO user) {
        Response createdResponse = kcAdminClient.createAccountHolder(user);
        return ResponseEntity.status(createdResponse.getStatus()).build();
    }

    @PostMapping(value = "/create/admin")
    public ResponseEntity<?> createKeycloakUser(@RequestBody AdminDTO user) {
        Response createdResponse = kcAdminClient.createAdmin(user);
        return ResponseEntity.status(createdResponse.getStatus()).build();
    }


    // GET
    // **************************************************************************
    @GetMapping("/get/user/by-name/{username}")
    public ResponseEntity<Object> getByUsername(@PathVariable String username) {
        if (adminService.getAccountHolderByUsername(username) != null){
            return ResponseEntity.ok(adminService.getAccountHolderByUsername(username));
        } else if (adminService.getAdminByUsername(username) != null) {
            return ResponseEntity.ok(adminService.getAdminByUsername(username));
        } else return ResponseEntity.badRequest().body(null);
    }

    @GetMapping("/get/user/by-id/{id}")
    public ResponseEntity<Object> getById(@PathVariable String id) {
        if (adminService.getAccountHolderById(id) != null){
            return ResponseEntity.ok(adminService.getAccountHolderById(id));
        } else if (adminService.getAdminById(id) != null) {
            return ResponseEntity.ok(adminService.getAdminById(id));
        } else return ResponseEntity.badRequest().body(null);
    }


    //**************************************************************
    //            -------       ACCOUNTS       -------
    //**************************************************************

    @PostMapping(value = "/create/account/checking")
    public ResponseEntity<String> createCheckingAccount(@RequestBody CheckingDTO account) {
       return adminService.createChecking(account);
    }


    // todo: create account credit
    // todo: create account savings



}
