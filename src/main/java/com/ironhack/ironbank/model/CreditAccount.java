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
@Setter
@Getter
@NoArgsConstructor
public class CreditAccount{

    public final static BigDecimal PENALTY_FEE = new BigDecimal("40");

    public final static BigDecimal MAX_CREDIT_LIMIT = new BigDecimal("100000");
    public final static BigDecimal DEFAULT_CREDIT_LIMIT = new BigDecimal("100");

    public final static BigDecimal MIN_INTEREST_RATE = new BigDecimal("0.1");
    public final static BigDecimal DEFAULT_INTEREST_RATE = new BigDecimal("0.2");

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID accountID;

    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID secretKey;

    @ManyToOne
    @JoinTable(
            name = "account_holder_primaary_credit_accounts",
            joinColumns = @JoinColumn(name="account_holder_id"),
            inverseJoinColumns = @JoinColumn(name= "credit_account_id")
    )
    @JsonIgnore
    AccountHolder primaryOwner;

    @ManyToOne
    @JoinTable(
            name = "account_holder_secondary_credit_accounts",
            joinColumns = @JoinColumn(name="account_holder_id"),
            inverseJoinColumns = @JoinColumn(name= "credit_account_id")
    )
    @JsonIgnore    AccountHolder secondaryOwner;

    Money balance;

    Instant creationDate;

    BigDecimal creditLimit;

    BigDecimal interestRate;

    @Enumerated(EnumType.STRING)
    AccountStatus accountStatus;


    public CreditAccount(AccountHolder primaryOwner, AccountHolder secondaryOwner, Money balance, BigDecimal creditLimit, BigDecimal interestRate) {
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        this.balance = balance;
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;

    }


    public static CreditAccount createAccount (AccountHolder primaryOwner, AccountHolder secondaryOwner, Money balance, BigDecimal creditLimit, BigDecimal interestRate){
       CreditAccount newAccount = new CreditAccount(primaryOwner,secondaryOwner,balance,creditLimit,interestRate);
        newAccount.secretKey = UUID.randomUUID();
        newAccount.creationDate = Instant.now();
        newAccount.accountStatus = AccountStatus.OPEN;

        return newAccount;
    }


    @Override
    public String toString() {
        return "Credit Account - " + "accountID: " + accountID + "\n"+
                "secretKey: " + secretKey + "\n"+
                "primaryOwner: \n" +
                "  Owner ID: "+ primaryOwner.getId() + "\n" +
                "  Owner Name: "+ primaryOwner.getFirstname() +" "+ primaryOwner.getLastname()+ "\n" +
                "secondaryOwner: \n" +
                "  Owner ID: "+ secondaryOwner.getId() + "\n" +
                "  Owner Name: "+ secondaryOwner.getFirstname() +" "+ secondaryOwner.getLastname()+ "\n" +
                "balance: " + balance + "\n"+
                "creationDate: " + creationDate + "\n"+
                "creditLimit: " + creditLimit + "\n"+
                "interestRate: " + interestRate + "\n"+
                "accountStatus: " + accountStatus;
    }
}
