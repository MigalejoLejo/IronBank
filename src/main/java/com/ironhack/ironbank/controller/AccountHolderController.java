package com.ironhack.ironbank.controller;


import com.ironhack.ironbank.service.AccountHolderService;
import com.ironhack.ironbank.users.DTO.AccountHolderDTO;
import lombok.extern.java.Log;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.Response;
import java.security.Principal;


@RestController
@Log
@RequestMapping("/accountholder")
public class AccountHolderController {

    @Autowired
    AccountHolderService accountHolderService;

    @GetMapping("/{username}")
    public ResponseEntity<AccountHolderDTO> getByUsername(@PathVariable String username, Principal principal){
       String tokenId = accountHolderService.getByUsername(principal.getName()).getId();
       String requestedUserId = accountHolderService.getByUsername(username).getId();

       if (requestedUserId==tokenId){
           log.info("TokenID and requested UserID are equal. Access granted");
           return ResponseEntity.ok(accountHolderService.getByUsername(username));
       }else{
           log.severe("TokenID and requested UserID are not equal. Forbidden Access");
           log.warning("TokenID: "+tokenId);
           log.warning("RequestedID: "+requestedUserId);
           return ResponseEntity.status(HttpStatus.SC_FORBIDDEN).body(null);
       }
    }



}
