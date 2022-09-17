package com.ironhack.ironbank.controller;


import com.ironhack.ironbank.config.KeycloakProvider;
import com.ironhack.ironbank.DTO.KeycloakUser;
import com.ironhack.ironbank.DTO.TokenRequest;
import com.ironhack.ironbank.service.KeycloakAdminClientService;
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
    private final KeycloakAdminClientService kcAdminClient;
    private final KeycloakProvider kcProvider;


    public UserController(KeycloakAdminClientService kcAdminClient, KeycloakProvider kcProvider) {
        this.kcProvider = kcProvider;
        this.kcAdminClient = kcAdminClient;
    }


    @PostMapping(value = "/create/{role}")
    public ResponseEntity<?> createKeycloakUser(@RequestBody KeycloakUser user, @PathVariable String role) {
        Response createdResponse = kcAdminClient.createKeycloakUser(user, role);
        return ResponseEntity.status(createdResponse.getStatus()).build();
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
