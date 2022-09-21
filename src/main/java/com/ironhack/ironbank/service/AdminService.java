package com.ironhack.ironbank.service;


import com.ironhack.ironbank.DTO.*;
import com.ironhack.ironbank.config.KeycloakProvider;
import com.ironhack.ironbank.helpclasses.Money;
import com.ironhack.ironbank.model.*;
import com.ironhack.ironbank.repository.AdminRepository;
import com.ironhack.ironbank.helpclasses.RealmGroup;
import lombok.Getter;
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

@Getter
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


    /**
     * Creates a new Checking account or a new Student Checking account depending on the
     * user's age. Once created it returns a response and a message with the objects .toString().
     * @param account: CheckingDTO
     * @return
     */
    public ResponseEntity<String> createChecking(CheckingDTO account) {
        AccountHolder owner;
        AccountHolder secondaryOwner = null;
        Money accountBalance = new Money (new BigDecimal(account.getBalance()));


        // CHECK FOR PRIMARY OWNER
        // -------------------------------------------------------------------------------------------
        if (accountHolderService.getById(account.getPrimaryOwnerId()) == null) {
            return ResponseEntity
                    .status(org.springframework.http.HttpStatus.NOT_FOUND)
                    .body("Owner not Found");
        } else {
            owner = AccountHolder.fromDTO(accountHolderService.getById(account.getPrimaryOwnerId()));
        }
        // -------------------------------------------------------------------------------------------


        // CHECK FOR SECONDARY OWNER
        // -------------------------------------------------------------------------------------------
        if (accountHolderService.getById(account.getSecondaryOwnerId()) == null) {
            log.info("No secondary owner provided");
        } else {
            secondaryOwner = AccountHolder.fromDTO(accountHolderService.getById(account.getSecondaryOwnerId()));
        }


        // CHECK FOR STUDENT ACCOUNT POSSIBILITY
        // -------------------------------------------------------------------------------------------
        if (validateAgeForStudent(owner.getDateOfBirth())) {
            StudentChecking newAccount = StudentChecking.createAccount(owner, accountBalance);
            newAccount.setSecondaryOwner(secondaryOwner);

            return ResponseEntity.ok("new Account Create: \n" + studentCheckingService.add(newAccount).toString());

        } else if (accountBalance.getAmount().compareTo(Checking.MINIMUM_BALANCE) < 0) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.NOT_IMPLEMENTED).body("Balance is not enough. The minimum Balance is 250,-€");

        } else if (accountBalance.getAmount().compareTo(Checking.MINIMUM_BALANCE) > 0) {
            Checking newAccount = Checking.createAccount(owner, accountBalance);
            newAccount.setSecondaryOwner(secondaryOwner);

            return ResponseEntity.ok("new Account Created: \n" + checkingService.add(newAccount).toString());
        } else {

            return ResponseEntity.status(org.springframework.http.HttpStatus.NOT_IMPLEMENTED).body("Account was not created");
        }
    }


    /**
     * Creates a new Credit Account.
     * Once created it returns a response and a message with the objects .toString().
     * @param account: CreditDTO
     * @return
     */
    public ResponseEntity<String> createCredit(CreditDTO account) {

        AccountHolder owner;
        AccountHolder secondaryOwner = null;
        Money balance = new Money (new BigDecimal(account.getBalance()));
        BigDecimal creditLimit;
        BigDecimal interestRate;


        // CHECK FOR PRIMARY OWNER
        // -------------------------------------------------------------------------------------------
        if (accountHolderService.getById(account.getPrimaryOwnerId()) == null) {
            return ResponseEntity
                    .status(org.springframework.http.HttpStatus.NOT_FOUND)
                    .body("Owner not Found");
        } else {
            owner = AccountHolder.fromDTO(accountHolderService.getById(account.getPrimaryOwnerId()));
        }
        // -------------------------------------------------------------------------------------------


        // CHECK FOR SECONDARY OWNER
        // -------------------------------------------------------------------------------------------
        if (accountHolderService.getById(account.getSecondaryOwnerId()) == null) {
            log.info("No secondary owner provided");
        } else {
            secondaryOwner = AccountHolder.fromDTO(accountHolderService.getById(account.getSecondaryOwnerId()));
        }
        // -------------------------------------------------------------------------------------------


        // CHECK FOR RIGHT CREDIT LIMIT
        // -------------------------------------------------------------------------------------------
        if (account.getCreditLimit() == null ) {
            creditLimit = CreditAccount.DEFAULT_CREDIT_LIMIT;
        } else if (account.getCreditLimit().isEmpty()) {
            creditLimit = CreditAccount.DEFAULT_CREDIT_LIMIT;
        }else{
            creditLimit = new BigDecimal(account.getCreditLimit());
        }
        // -------------------------------------------------------------------------------------------


        // CHECK FOR RIGHT INTEREST RATE
        // -------------------------------------------------------------------------------------------
        if (account.getInterestRate() == null ) {
            interestRate = CreditAccount.DEFAULT_INTEREST_RATE;
        }else if (account.getInterestRate().isEmpty()){
            interestRate = CreditAccount.DEFAULT_INTEREST_RATE;
        } else if (new BigDecimal(account.getInterestRate()).compareTo(CreditAccount.MIN_INTEREST_RATE)<0){
           interestRate = CreditAccount.DEFAULT_INTEREST_RATE;
        } else {
            interestRate = new BigDecimal(account.getInterestRate());
        }
        // -------------------------------------------------------------------------------------------


        // CREATE SAVE AND RETURN
        CreditAccount newAccount = CreditAccount.createAccount(owner,secondaryOwner,balance, creditLimit, interestRate);
        return ResponseEntity.ok("New CREDIT Account Created: \n" + creditAccountService.add(newAccount).toString());
    }


    /**
     * Creates a new Savings Account.
     * Once created it returns a response and a message with the objects .toString().
     * @param account: SavingsDTO
     * @return
     */
    public ResponseEntity<String> createSavings(SavingsDTO account) {

        AccountHolder owner;
        AccountHolder secondaryOwner = null;
        Money balance = new Money (new BigDecimal(account.getBalance()));
        BigDecimal interestRate;


        // CHECK FOR PRIMARY OWNER
        // -------------------------------------------------------------------------------------------
        if (accountHolderService.getById(account.getPrimaryOwnerId()) == null) {
            return ResponseEntity
                    .status(org.springframework.http.HttpStatus.NOT_FOUND)
                    .body("Owner not Found");
        } else {
            owner = AccountHolder.fromDTO(accountHolderService.getById(account.getPrimaryOwnerId()));
        }
        // -------------------------------------------------------------------------------------------


        // CHECK FOR SECONDARY OWNER
        // -------------------------------------------------------------------------------------------
        if (accountHolderService.getById(account.getSecondaryOwnerId()) == null) {
            log.info("No secondary owner provided");
        } else {
            secondaryOwner = AccountHolder.fromDTO(accountHolderService.getById(account.getSecondaryOwnerId()));
        }
        // -------------------------------------------------------------------------------------------


        // CHECK FOR RIGHT INTEREST RATE
        // -------------------------------------------------------------------------------------------
        if (account.getInterestRate() == null ) {
            interestRate = Savings.DEFAULT_INTEREST_RATE;
        }else if (account.getInterestRate().isEmpty()){
            interestRate = Savings.DEFAULT_INTEREST_RATE;
        } else if (new BigDecimal(account.getInterestRate()).compareTo(Savings.DEFAULT_INTEREST_RATE)<0){
            interestRate = Savings.DEFAULT_INTEREST_RATE;
        } else if (new BigDecimal(account.getInterestRate()).compareTo(Savings.MAX_INTEREST_RATE)>0){
            interestRate = Savings.MAX_INTEREST_RATE;
        } else {
            interestRate = new BigDecimal(account.getInterestRate());
        }
        // -------------------------------------------------------------------------------------------


        // CREATE SAVE AND RETURN
        Savings newAccount = Savings.createAccount(owner,secondaryOwner,balance,interestRate);
        return ResponseEntity.ok("New SAVINGS Account Created: \n" + savingsService.add(newAccount).toString());
    }


    /**
     * A Method to determine User's Age
     * @param date: LocalDate
     * @return
     */
    Boolean validateAgeForStudent(LocalDate date) {
        LocalDate currentDate = LocalDate.now();
        return Period.between(date, currentDate).getYears() >= 24;
    }




    /**
     * WARNING!! DELETES ALL USERS FROM KEYCLOAK
     */
    public void deleteUsers (){

        var userList = kcProvider.getInstance().realm(realm).users().list();

        for (UserRepresentation u: userList){
            kcProvider.getInstance().realm(realm).users().delete(u.getId());
        }
    }
}


