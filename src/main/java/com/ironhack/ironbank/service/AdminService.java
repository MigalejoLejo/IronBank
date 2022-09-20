package com.ironhack.ironbank.service;


import com.ironhack.ironbank.DTO.CheckingDTO;
import com.ironhack.ironbank.config.KeycloakProvider;
import com.ironhack.ironbank.DTO.AccountHolderDTO;
import com.ironhack.ironbank.DTO.AdminDTO;
import com.ironhack.ironbank.helpclasses.Money;
import com.ironhack.ironbank.model.Checking;
import com.ironhack.ironbank.model.StudentChecking;
import com.ironhack.ironbank.repository.AdminRepository;
import com.ironhack.ironbank.helpclasses.RealmGroup;
import com.ironhack.ironbank.model.AccountHolder;
import com.ironhack.ironbank.model.Admin;
import lombok.extern.java.Log;
import org.apache.http.HttpStatus;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.Collections;
import java.util.List;


@Service
@Log
public class AdminService {
    private final KeycloakProvider kcProvider;
    @Value("${keycloak.realm}")
    public String realm;

    @Value("${keycloak.resource}")
    public String clientId;

    final AdminRepository adminRepo;

    final AccountHolderService accountHolderService;
    final CheckingService checkingService;
    final StudentCheckingService studentCheckingService;
    final SavingsService savingsService;
    final CreditAccountService creditAccountService;


    public AdminService(KeycloakProvider keycloakProvider, AdminRepository adminRepo, AccountHolderService accountHolderService, CheckingService checkingService, StudentCheckingService studentCheckingService, SavingsService savingsService, CreditAccountService creditAccountService) {
        this.kcProvider = keycloakProvider;
        this.adminRepo = adminRepo;
        this.accountHolderService = accountHolderService;
        this.checkingService = checkingService;
        this.studentCheckingService = studentCheckingService;
        this.savingsService = savingsService;
        this.creditAccountService = creditAccountService;
    }


    /**
     * Creates Credentials needed to create new users. Takes a String as a param.
     *
     * @param password String
     */
    private static CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }


    //**************************************************************
    //            -------       USERS       -------
    //**************************************************************


    // ADD TO KEYCLOAK
    //--------------------------------------------------------------

    public Response createAccountHolder(AccountHolderDTO user) {

        // Check if the entered Username is correct.
        if (user.getUsername().contains(" ")) {
            log.severe("BAD REQUEST - Username contains spaces");
            return Response
                    .status(HttpStatus.SC_BAD_REQUEST, "Username contains spaces")
                    .build();
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
        kcUser.setGroups(List.of(RealmGroup.ACCOUNTHOLDERS));

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
            AccountHolder newAccountHolder = addAccountHolder(AccountHolder.fromDTO(user));
            log.info("Account holder with id: " + newAccountHolder.getId() + " added to Database");

        }
        return response;
    }

    public Response createAdmin(AdminDTO user) {

        // Check if the entered Username is correct.
        if (user.getUsername().contains(" ")) {
            log.severe("BAD REQUEST - Username contains spaces");
            return Response
                    .status(HttpStatus.SC_BAD_REQUEST, "Username contains spaces")
                    .build();
        }

        // Proceed to create user in KeyCloak and a Response
        var adminKeycloak = kcProvider.getInstance();
        UsersResource usersResource = kcProvider.getInstance().realm(realm).users();
        CredentialRepresentation credentialRepresentation = createPasswordCredentials(user.getPassword());

        UserRepresentation kcUser = new UserRepresentation();

        kcUser.setUsername(user.getUsername());
        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
        kcUser.setEmail(user.getEmail());
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(false);
        kcUser.setGroups(List.of(RealmGroup.ADMINS));

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
            Admin newAdmin = addAdmin(Admin.fromDTO(user));
            log.info("Account holder with id: " + newAdmin.getId() + " added to Database");

        }
        return response;
    }


    // ADD TO DB
    //--------------------------------------------------------------

    public Admin addAdmin(Admin admin) {
        return adminRepo.save(admin);
    }

    public AccountHolder addAccountHolder(AccountHolder accountHolder) {
        return accountHolderService.add(accountHolder);
    }


    //  GET
    //--------------------------------------------------------------
    public AdminDTO getAdminByUsername(String username) {
        AdminDTO adminDTO = null;
        if (adminRepo.findByUsername(username) != null) {
            adminDTO = AdminDTO.fromEntity(adminRepo.findByUsername(username));
        }
        return adminDTO;
    }

    public AdminDTO getAdminById(String id) {
        AdminDTO adminDTO = null;
        if (adminRepo.findById(id).isPresent()) {
            adminDTO = AdminDTO.fromEntity(adminRepo.findById(id).orElseThrow());
        }
        return adminDTO;
    }

    public AccountHolderDTO getAccountHolderByUsername(String username) {
        AccountHolderDTO accountHolderDTO = null;
        if (accountHolderService.getByUsername(username) != null) {
            accountHolderDTO = accountHolderService.getByUsername(username);
        }
        return accountHolderDTO;
    }

    public AccountHolderDTO getAccountHolderById(String id) {
        AccountHolderDTO accountHolderDTO = null;
        if (accountHolderService.getById(id) != null) {
            accountHolderDTO = accountHolderService.getById(id);
        }
        return accountHolderDTO;
    }


    //**************************************************************
    //            -------       ACCOUNTS       -------
    //**************************************************************


    // CREATE ACCOUNTS
    //--------------------------------------------------------------


    public ResponseEntity<String> createChecking(CheckingDTO account) {
        AccountHolder owner;
        AccountHolder secondaryOwner = null;
        BigDecimal balance = new BigDecimal(account.getBalance());
        Money accountBalance;

        // First check that the user exists
        if (accountHolderService.getById(account.getPrimaryOwnerId()) == null) {
            return ResponseEntity
                    .status(org.springframework.http.HttpStatus.NOT_FOUND)
                    .body("Owner not Found");
        } else {
            owner = AccountHolder.fromDTO(accountHolderService.getById(account.getPrimaryOwnerId()));
        }

        // then look if there is a secondary owner available
        if (accountHolderService.getById(account.getSecondaryOwnerId()) == null) {
            log.info("No secondary owner provided");
        } else {
            secondaryOwner = AccountHolder.fromDTO(accountHolderService.getById(account.getSecondaryOwnerId()));
        }

        // check if the age is right for a student account
        if (validateAgeForStudent(owner.getDateOfBirth())) {
            accountBalance = new Money(balance);
            StudentChecking newAccount = StudentChecking.createAccount(owner, accountBalance);

            if (secondaryOwner != null) {
                newAccount.setSecondaryOwner(secondaryOwner);
            }

            return ResponseEntity.ok("new Account Create: \n" + studentCheckingService.add(newAccount).toString());

            // if not, then create a normal checking account
            // first we check that there is enough balance
        } else if (balance.compareTo(Checking.MINIMUM_BALANCE) < 0) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.NOT_IMPLEMENTED).body("Balance is not enough. The minimum Balance is 250,-€");

            // if everything is fine we create a checking account.
        } else if (balance.compareTo(Checking.MINIMUM_BALANCE) > 0) {
            accountBalance = new Money(balance);
            Checking newAccount = Checking.createAccount(owner, accountBalance);

            if (secondaryOwner != null) {
                newAccount.setSecondaryOwner(secondaryOwner);
            }

            return ResponseEntity.ok("new Account Created: \n" + checkingService.add(newAccount).toString());

        } else {
            return ResponseEntity.status(org.springframework.http.HttpStatus.NOT_IMPLEMENTED).body("Account was not created");
        }
    }

    Boolean validateAgeForStudent(LocalDate date) {
        LocalDate currentDate = LocalDate.now();
        return Period.between(date, currentDate).getYears() >= 24;
    }



    public void deleteUsers (){

        var userList = kcProvider.getInstance().realm(realm).users().list();

        for (UserRepresentation u: userList){
            kcProvider.getInstance().realm(realm).users().delete(u.getId());
        }
    }
}


