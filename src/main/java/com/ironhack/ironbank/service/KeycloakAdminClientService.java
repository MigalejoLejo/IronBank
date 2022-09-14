package com.ironhack.ironbank.service;


import com.ironhack.ironbank.config.KeycloakProvider;
import com.ironhack.ironbank.http.requests.RealmGroup;
import com.ironhack.ironbank.users.DTO.AccountHolderDTO;
import com.ironhack.ironbank.users.model.AccountHolder;
import lombok.extern.java.Log;
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




    private static CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }




    public Response createAccountHolder(AccountHolderDTO user) {

        String group = RealmGroup.ACCOUNTHOLDERS;

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
            AccountHolder newAccountHolder = acHoService.add(AccountHolder.fromDTO(user));
            log.info("User with id: " + newAccountHolder.getId() + " added to Database");

        }
        return response;
    }

}