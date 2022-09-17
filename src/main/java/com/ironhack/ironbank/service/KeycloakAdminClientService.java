package com.ironhack.ironbank.service;


import com.ironhack.ironbank.config.KeycloakProvider;
import com.ironhack.ironbank.DTO.AccountHolderDTO;
import com.ironhack.ironbank.DTO.AdminDTO;
import com.ironhack.ironbank.DTO.KeycloakUser;
import com.ironhack.ironbank.roles.RealmGroup;
import com.ironhack.ironbank.roles.UserRole;
import com.ironhack.ironbank.model.AccountHolder;
import com.ironhack.ironbank.model.Admin;
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

        // Check if the entered Username is correct.
        if (user.getUsername().contains(" ")){
            log.severe("BAD REQUEST - Username contains spaces");
            return Response
                    .status(HttpStatus.SC_BAD_REQUEST,"Username contains spaces")
                    .build();
        }

        // todo: check if user already exist and provide that info

        // Set role accordingly and check if the needed information is complete for either an AccountHolder or an Admin
        switch (role.toLowerCase()){
            case UserRole.ACCOUNTHOLDER -> {
                group = RealmGroup.ACCOUNTHOLDERS;
                if (user.getPassword() == null
                        || user.getEmail() == null
                        || user.getFirstname() == null
                        || user.getLastname() == null
                        || user.getDateOfBirth() == null
                        || user.getStreet() == null
                        || user.getNumber() == null
                        || user.getPostalCode() == null
                        || user.getCity() == null
                        || user.getLand() == null ) {
                    log.info("BAD REQUEST - Accountholder Information is incomplete");
                    return Response.status(HttpStatus.SC_BAD_REQUEST,"Accountholder Information is incomplete").build();
                }
            }
            case UserRole.ADMIN -> {
                group = RealmGroup.ADMINS;
                if (user.getPassword() == null || user.getEmail() == null) {
                    log.info("BAD REQUEST - Admin Information is incomplete");
                    return Response.status(HttpStatus.SC_BAD_REQUEST,"Admin Information is incomplete").build();
                }
            }
            default -> {
                log.info("BAD REQUEST - The Role entered does not exist");
                return Response.status(HttpStatus.SC_BAD_REQUEST,"The role entered does not exist").build();
            }
        }

        // Proceed to create user in KeyCloak and a Response
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

        // If the user was created properly the user is added to the DB as well

        // First we get the created user from KeyCloak
        if (response.getStatus() == 201) {
            List<UserRepresentation> userList = adminKeycloak
                    .realm(realm)
                    .users()
                    .search(kcUser.getUsername())
                    .stream()
                    //.filter(userRep -> userRep.getUsername().equals(kcUser.getUsername()))
                    .toList();

            // here we retrieve the user from KeyCloak to get the assigned ID
            var createdUser = userList.get(0);
            log.info("User with id: " + createdUser.getId() + " created");

            // The ID is associated to the DTO.
            user.setId(createdUser.getId());

            // Now a full Entity (ID Included) is created and finally added to the DB
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