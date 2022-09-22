package com.ironhack.ironbank.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ironhack.ironbank.helpclasses.AccountStatus;
import com.ironhack.ironbank.helpclasses.Money;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class StudentChecking {

    public final static BigDecimal MINIMUM_BALANCE = new BigDecimal("0");
    public final static BigDecimal PENALTY_FEE = new BigDecimal("40");
    public final static BigDecimal MONTHLY_MAINTENANCE_FEE = new BigDecimal("0");

    @Id
    @GeneratedValue
    UUID accountID;

    @GeneratedValue
    UUID secretKey;

    @ManyToOne
    @JoinTable(
            name = "account_holder_primaary_student_checkings",
            joinColumns = @JoinColumn(name="account_holder_id"),
            inverseJoinColumns = @JoinColumn(name= "student_checking_id")
    )
    @JsonIgnore
    AccountHolder primaryOwner;

    @ManyToOne
    @JoinTable(
            name = "account_holder_secondary_student_checkings",
            joinColumns = @JoinColumn(name="account_holder_id"),
            inverseJoinColumns = @JoinColumn(name= "student_checking_id")
    )
    @JsonIgnore
    AccountHolder secondaryOwner;

    Money balance;

    Instant creationDate;

    @Enumerated(EnumType.STRING)
    AccountStatus accountStatus;

    public StudentChecking(AccountHolder primaryOwner,Money balance) {
        this.primaryOwner = primaryOwner;
        this.balance = balance;
    }


    public static StudentChecking createAccount(AccountHolder primaryOwner, Money balance){
        StudentChecking newAccount = new StudentChecking(primaryOwner,balance);
        newAccount.secretKey = UUID.randomUUID();
        newAccount.creationDate = Instant.now();
        newAccount.accountStatus = AccountStatus.OPEN;
        return newAccount;
    }

    @Override
    public String toString() {
        return "StudentChecking Account - " + "accountID: " + accountID + "\n"+
                "secretKey: " + secretKey + "\n"+
                "primaryOwner: \n" +
                "  Owner ID: "+ primaryOwner.getId() + "\n" +
                "  Owner Name: "+ primaryOwner.getFirstname() +" "+ primaryOwner.getLastname()+ "\n" +
                "secondaryOwner: \n" +
                "  Owner ID: "+ secondaryOwner.getId() + "\n" +
                "  Owner Name: "+ secondaryOwner.getFirstname() +" "+ secondaryOwner.getLastname()+ "\n" +
                "balance: " + balance + "\n"+
                "creationDate: " + creationDate + "\n"+
                "accountStatus: " + accountStatus ;
    }
}
