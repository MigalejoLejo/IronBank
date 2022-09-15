package com.ironhack.ironbank.service;


import com.ironhack.ironbank.config.KeycloakProvider;
import com.ironhack.ironbank.users.DTO.AccountHolderDTO;
import com.ironhack.ironbank.users.DTO.AdminDTO;
import com.ironhack.ironbank.users.DTO.KeycloakUser;
import com.ironhack.ironbank.users.roles.RealmGroup;
import com.ironhack.ironbank.users.roles.UserRole;
import com.ironhack.ironbank.users.model.AccountHolder;
import com.ironhack.ironbank.users.model.Admin;
import lombok.extern.java.Log;
import org.apache.http.HttpStatus;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;


@Service
@Log
public class KeycloakAdminClientService {
    private final KeycloakProvider kcProvider;
    final AdminService adminService;
    final AccountHolderService acHoService;
    @Value("${keycloak.realm}")
    public String realm;

    @Value("${keycloak.resource}")
    public String clientId;

    public KeycloakAdminClientService(KeycloakProvider keycloakProvider, AdminService adminService, AccountHolderService acHoService) {
        this.kcProvider = keycloakProvider;
        this.adminService = adminService;
        this.acHoService = acHoService;
    }


    /**
     * Creates Credentials needed to create new users. Takes a String as a param.
     * @param password String
     */
    private static CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }


    /**
     * Creates a new keycloack user and assigned him a role. Then add the user to the database
     * @param user KeycloakUser
     * @param role String indicating the desired role
     */
    public Response createKeycloakUser(KeycloakUser user, String role) {

        String group = null;

        if (user.getUsername().contains(" ")){
            log.severe("BAD REQUEST - Username contains spaces");
            return Response
                    .status(HttpStatus.SC_BAD_REQUEST,"Username contains spaces")
                    .encoding("Encoding: Username contains spaces")
                    .tag("tag: username contains spaces")
                    .build();
        }

        switch (role.toLowerCase()){
            case UserRole.ACCOUNTHOLDER -> {
                group = RealmGroup.ACCOUNTHOLDERS;
                if (user.getStreet() == null
                        || user.getNumber() == null
                        || user.getPostalCode() == null
                        || user.getCity() == null
                        || user.getLand() == null ) {
                    log.info("BAD REQUEST - The Address is incomplete");
                    return Response.status(HttpStatus.SC_BAD_REQUEST,"The Address is incomplete").build();

                }
            }
            case UserRole.ADMIN -> {
                group = RealmGroup.ADMINS;
            }
            default -> {
                log.info("BAD REQUEST - The Role entered does not exist");
                return Response.status(HttpStatus.SC_BAD_REQUEST,"The role entered does not exist").build();
            }
        }


        var adminKeycloak = kcProvider.getInstance();
        UsersResource usersResource = kcProvider.getInstance().realm(realm).users();
        CredentialRepresentation credentialRepresentation = createPasswordCredentials(user.getPassword());

        UserRepresentation kcUser = new UserRepresentation();

        kcUser.setUsername(user.getUsername());
        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
        kcUser.setFirstName(user.getFirstname());
        kcUser.setLastName(user.getLastname());
        kcUser.setEmail(user.getEmail());
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(false);
        kcUser.setGroups(List.of(group));

        Response response = usersResource.create(kcUser);

        if (response.getStatus() == 201) {
            List<UserRepresentation> userList = adminKeycloak
                    .realm(realm)
                    .users()
                    .search(kcUser.getUsername())
                    .stream()
                    //.filter(userRep -> userRep.getUsername().equals(kcUser.getUsername()))
                    .toList();

            var createdUser = userList.get(0);
            log.info("User with id: " + createdUser.getId() + " created");


            user.setId(createdUser.getId());

            switch (role.toLowerCase()){
                case UserRole.ACCOUNTHOLDER -> {
                    AccountHolderDTO newAccountHolder = acHoService.add(AccountHolder.fromKeycloakUser(user));
                    log.info("Account holder with id: " + newAccountHolder.getId() + " added to Database");
                }
                case UserRole.ADMIN -> {
                    AdminDTO newAdmin = adminService.add(Admin.fromKeycloakUser(user));
                    log.info("Admin with id: " + newAdmin.getId() + " added to Database");
                }
            }
        }
        return response;
    }




}