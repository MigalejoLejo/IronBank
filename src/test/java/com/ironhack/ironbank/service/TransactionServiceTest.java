package com.ironhack.ironbank.service;

import com.ironhack.ironbank.DTO.AccountHolderDTO;
import com.ironhack.ironbank.DTO.AdminDTO;
import com.ironhack.ironbank.DTO.TransactionDTO;
import com.ironhack.ironbank.helpclasses.Money;
import com.ironhack.ironbank.model.AccountHolder;
import com.ironhack.ironbank.model.Admin;
import com.ironhack.ironbank.model.Checking;
import com.ironhack.ironbank.model.Savings;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransactionServiceTest {

    @Autowired
    AdminService adminService;

    @Autowired
    TransactionService transactionService;




    @BeforeEach
    void setUp() {

//        adminService.deleteUsers();
//
//        AdminDTO admin = new AdminDTO(null, "testadmin", "123456", "admin@email.com");
//        AccountHolderDTO user1 = new AccountHolderDTO(null, "user1", "123456", "user1@email.com", "paco", "", LocalDate.of(1985, 12, 5), "null", "null", "null", "null", "null", "null");
//        AccountHolderDTO user2 = new AccountHolderDTO(null, "user2", "123456", "user2@email.com", "lucy", "null", LocalDate.of(1985, 12, 5), "null", "null", "null", "null", "null", "null");
//        AccountHolderDTO user3 = new AccountHolderDTO(null, "user3", "123456", "user3@email.com", "laura", "null", LocalDate.of(2005, 12, 5), "null", "null", "null", "null", "null", "null");
//
//
//        adminService.createAdmin(admin);
//        adminService.createAccountHolder(user1);
//        adminService.createAccountHolder(user2);
//        adminService.createAccountHolder(user3);
//
//        Checking checking1 = Checking.createAccount(AccountHolder.fromDTO(user1), null, new Money(new BigDecimal("300")));
//        Checking checking2 = Checking.createAccount(AccountHolder.fromDTO(user2), null, new Money(new BigDecimal("400")));
//        Checking checking3 = Checking.createAccount(AccountHolder.fromDTO(user3), null, new Money(new BigDecimal("500")));
//        Checking checking4 = Checking.createAccount(AccountHolder.fromDTO(user1), null, new Money(new BigDecimal("230")));
//        Checking checking5 = Checking.createAccount(AccountHolder.fromDTO(user1), null, new Money(new BigDecimal("25")));
//
//        checking1.setSecondaryOwner(AccountHolder.fromDTO(user2));
//        checking2.setSecondaryOwner(AccountHolder.fromDTO(user3));
//        checking3.setSecondaryOwner(AccountHolder.fromDTO(user1));
//
//        Savings savings1 = Savings.createAccount(AccountHolder.fromDTO(user1), null, new Money(new BigDecimal("25")), null);
//
//
//        adminService.getCheckingService().add(checking1);
//        adminService.getCheckingService().add(checking2);
//        adminService.getCheckingService().add(checking3);
//        adminService.getCheckingService().add(checking4);
//        adminService.getCheckingService().add(checking5);
//        adminService.getSavingsService().add(savings1);

    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void userMakeTransaction() {
        var aa1 = adminService.getAdminByUsername("testAdmin");
        var uu1 = adminService.getAccountHolderByUsername("user1");
        var uu2 = adminService.getAccountHolderByUsername("user3");
        var uu3 = adminService.getAccountHolderByUsername("user3");

        var newTransaction = new TransactionDTO(uu1.getPrimaryCheckings().get(0).getAccountID().toString(), uu2.getPrimaryCheckings().get(0).getAccountID().toString(), "test money", "30");

        transactionService.userMakeTransaction(uu1.getId(), newTransaction);
    }
}