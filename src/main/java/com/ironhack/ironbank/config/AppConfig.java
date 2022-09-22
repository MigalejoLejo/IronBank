package com.ironhack.ironbank.config;


import com.ironhack.ironbank.DTO.AccountHolderDTO;
import com.ironhack.ironbank.DTO.AdminDTO;
import com.ironhack.ironbank.helpclasses.Money;
import com.ironhack.ironbank.model.AccountHolder;
import com.ironhack.ironbank.model.Checking;
import com.ironhack.ironbank.model.Savings;
import com.ironhack.ironbank.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;

@Configuration
public class AppConfig {


    @Autowired
    AdminService adminService;



    @Bean
    void config (){
        adminService.deleteUsers();

        AdminDTO admin = new AdminDTO(null,"testadmin", "123456", "admin@email.com");
        AccountHolderDTO user1 = new AccountHolderDTO(null,"user1", "123456", "user1@email.com","paco","", LocalDate.of(1985,12,05),"null","null","null","null","null","null");
        AccountHolderDTO user2 = new AccountHolderDTO(null,"user2", "123456", "user2@email.com","lucy","null", LocalDate.of(1985,12,05),"null","null","null","null","null","null");
        AccountHolderDTO user3 = new AccountHolderDTO(null,"user3", "123456", "user3@email.com","laura","null", LocalDate.of(2005,12,05),"null","null","null","null","null","null");


        var a = adminService.createAdmin(admin);
        var u1 = adminService.createAccountHolder(user1);
        adminService.createAccountHolder(user2);
        adminService.createAccountHolder(user3);

        Checking checking1 = Checking.createAccount(AccountHolder.fromDTO(user1),new Money(new BigDecimal("300")));
        Checking checking2 = Checking.createAccount(AccountHolder.fromDTO(user2),new Money(new BigDecimal("400")));
        Checking checking3 = Checking.createAccount(AccountHolder.fromDTO(user3),new Money(new BigDecimal("500")));
        Checking checking4 = Checking.createAccount(AccountHolder.fromDTO(user1),new Money(new BigDecimal("230")));
        Checking checking5 = Checking.createAccount(AccountHolder.fromDTO(user1),new Money(new BigDecimal("25")));

        checking1.setSecondaryOwner(AccountHolder.fromDTO(user2));
        checking2.setSecondaryOwner(AccountHolder.fromDTO(user3));
        checking3.setSecondaryOwner(AccountHolder.fromDTO(user1));

        Savings savings1 = Savings.createAccount(AccountHolder.fromDTO(user1),null,new Money(new BigDecimal("25")),null);


        adminService.getCheckingService().add(checking1);
        adminService.getCheckingService().add(checking2);
        adminService.getCheckingService().add(checking3);
        adminService.getCheckingService().add(checking4);
        adminService.getCheckingService().add(checking5);
        adminService.getSavingsService().add(savings1);

    }
}
