package com.ironhack.ironbank.service;


import com.ironhack.ironbank.config.KeycloakProvider;
import com.ironhack.ironbank.http.requests.KeycloakUser;
import com.ironhack.ironbank.http.requests.RealmGroup;
import lombok.extern.java.Log;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

import static com.ironhack.ironbank.http.requests.ClientRole.ACCOUNTHOLDER;
import static com.ironhack.ironbank.http.requests.ClientRole.ADMIN;


@Service
@Log
public class KeycloakAdminClientService {
    private final KeycloakProvider kcProvider;
    @Value("${keycloak.realm}")
    public String realm;

    @Value("${keycloak.resource}")
    public String clientId;


    public KeycloakAdminClientService(KeycloakProvider keycloakProvider) {
        this.kcProvider = keycloakProvider;
    }

    private static CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }

    public Response createKeycloakUser(KeycloakUser user, String role) {
        var adminKeycloak = kcProvider.getInstance();
        UsersResource usersResource = kcProvider.getInstance().realm(realm).users();
        CredentialRepresentation credentialRepresentation = createPasswordCredentials(user.getPassword());

        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(user.getUsername());
        kcUser.setFirstName(user.getFirstname());
        kcUser.setLastName(user.getLastname());
        kcUser.setEmail(user.getEmail());
        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(false);

        switch (role) {
            case ACCOUNTHOLDER -> kcUser.setGroups(List.of(RealmGroup.ACCOUNTHOLDERS));
            case ADMIN -> kcUser.setGroups(List.of(RealmGroup.ADMINS));
        }

        Response response = usersResource.create(kcUser);

        if (response.getStatus() == 201) {
            List<UserRepresentation> userList = adminKeycloak
                    .realm(realm)
                    .users()
                    .search(kcUser.getUsername())
                    .stream()
                    // the next line gives me an error: index out of bound
                    // .filter(userRep -> userRep.getUsername().equals(kcUser.getUsername()))
                    .toList();
            var createdUser = userList.get(0);
            log.info("User with id: " + createdUser.getId() + " created");
//            TODO you may add you logic to store and connect the keycloak user to the local user here
        }

        return response;

    }


}