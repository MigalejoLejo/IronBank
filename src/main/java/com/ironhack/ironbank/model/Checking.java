package com.ironhack.ironbank.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ironhack.ironbank.helpclasses.AccountStatus;
import com.ironhack.ironbank.helpclasses.Money;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.java.Log;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Log
public class Checking{

    public final static BigDecimal MINIMUM_BALANCE = new BigDecimal("250");
    public final static BigDecimal PENALTY_FEE = new BigDecimal("40");
    public final static BigDecimal MONTHLY_MAINTENANCE_FEE = new BigDecimal("12");

    @Id
    @GeneratedValue
    @Type(type = "org.hibernate.type.UUIDCharType")
    UUID accountID;

    @GeneratedValue
    @Type(type = "org.hibernate.type.UUIDCharType")
    UUID secretKey;

    @ManyToOne
    @JoinTable(
            name = "account_holder_primary_checkings",
            joinColumns = @JoinColumn(name="account_holder_id"),
            inverseJoinColumns = @JoinColumn(name= "checking_account_id")
    )
    @JsonIgnore
    AccountHolder primaryOwner;

    @ManyToOne
    @JoinTable(
            name = "account_holder_secondary_checkings",
            joinColumns = @JoinColumn(name="account_holder_id"),
            inverseJoinColumns = @JoinColumn(name= "checking_account_id")
    )
    @JsonIgnore
    AccountHolder secondaryOwner;

    Money balance;

    LocalDate creationDate;

    @Enumerated(EnumType.STRING)
    AccountStatus accountStatus;

    @OneToMany
    @JoinTable(
            name = "checking_transaction_list",
            joinColumns = @JoinColumn(name="checking_account_id"),
            inverseJoinColumns = @JoinColumn(name= "transaction_id")
    )
    @JsonIgnore
    List<Transaction> transactionList;



    public Checking(AccountHolder primaryOwner, AccountHolder secondaryOwner, Money balance) {
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        this.balance = balance;
    }

    public static Checking createAccount(AccountHolder primaryOwner, AccountHolder secondaryOwner, Money balance){
        Checking newAccount = new Checking(primaryOwner, secondaryOwner, balance);
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

        return "Checking Account - " + "accountID: " + accountID + "\n"+
                "secretKey: " + secretKey + "\n"+
                "primaryOwner: \n" +
                "  Owner ID: "+ primaryOwner.getId() + "\n" +
                "  Owner Name: "+ primaryOwner.getFirstname() +" "+ primaryOwner.getLastname()+ "\n" +
                secondaryToString +"\n" +
                "balance: " + balance + "\n"+
                "creationDate: " + creationDate + "\n"+
                "accountStatus: " + accountStatus ;
    }
}

