package com.ironhack.ironbank.controller;


import com.ironhack.ironbank.DTO.AccountHolderDTO;
import com.ironhack.ironbank.DTO.AdminDTO;
import com.ironhack.ironbank.config.KeycloakProvider;
import com.ironhack.ironbank.helpclasses.TokenRequest;
import com.ironhack.ironbank.service.AdminService;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;

@RestController
@RequestMapping("/user")
public class UserController {
    private final AdminService kcAdminClient;
    private final KeycloakProvider kcProvider;
    private final AdminService adminService;




    public UserController(AdminService kcAdminClient, KeycloakProvider kcProvider, AdminService adminService) {
        this.kcProvider = kcProvider;
        this.kcAdminClient = kcAdminClient;
        this.adminService = adminService;
    }


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

    @GetMapping("/get/{username}")
    public ResponseEntity<Object> getAccountholderByUsername(@PathVariable String username) {
        if (adminService.getAccountHolderByUsername(username) != null){
            return ResponseEntity.ok(adminService.getAccountHolderByUsername(username));
        } else if (adminService.getAdminByUsername(username) != null) {
            return ResponseEntity.ok(adminService.getAdminByUsername(username));
        } else return ResponseEntity.badRequest().body(null);
    }

    @PostMapping("/get-token")
    public ResponseEntity<AccessTokenResponse> login(@NotNull @RequestBody TokenRequest tokenRequest) {
        Keycloak keycloak = kcProvider.newKeycloakBuilderWithPasswordCredentials(tokenRequest.getEmail(), tokenRequest.getPassword()).build();

        AccessTokenResponse accessTokenResponse = null;
        try {
            accessTokenResponse = keycloak.tokenManager().getAccessToken();
            return ResponseEntity.status(HttpStatus.OK).body(accessTokenResponse);
        } catch (BadRequestException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(accessTokenResponse);
        }

    }


}
