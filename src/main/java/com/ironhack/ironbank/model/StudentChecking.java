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
import java.time.LocalDate;
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
            name = "account_holder_primary_student_checkings",
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

    LocalDate creationDate;

    @Enumerated(EnumType.STRING)
    AccountStatus accountStatus;

    public StudentChecking(AccountHolder primaryOwner, AccountHolder secondaryOwner, Money balance) {
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        this.balance = balance;
    }


    public static StudentChecking createAccount(AccountHolder primaryOwner, AccountHolder secondaryOwner, Money balance){
        StudentChecking newAccount = new StudentChecking(primaryOwner,secondaryOwner, balance);
        newAccount.secretKey = UUID.randomUUID();
        newAccount.creationDate = LocalDate.now();
        newAccount.accountStatus = AccountStatus.OPEN;
        return newAccount;
    }

    @Override
    public String toString() {
        String secondaryToString;
        if (secondaryOwner != null){
            secondaryToString =
                    "secondaryOwner: \n" +
                            "  Owner ID: "+ secondaryOwner.getId() + "\n" +
                            "  Owner Name: "+ secondaryOwner.getFirstname() +" "+ secondaryOwner.getLastname();
        } else {
            secondaryToString = "No Secondary Owner.";
        }

        return "StudentChecking Account - " + "accountID: " + accountID + "\n"+
                "secretKey: " + secretKey + "\n"+
                "primaryOwner: \n" +
                "  Owner ID: "+ primaryOwner.getId() + "\n" +
                "  Owner Name: "+ primaryOwner.getFirstname() +" "+ primaryOwner.getLastname()+ "\n" +
                secondaryToString + "\n" +
                "balance: " + balance + "\n"+
                "creationDate: " + creationDate + "\n"+
                "accountStatus: " + accountStatus ;
    }
}
