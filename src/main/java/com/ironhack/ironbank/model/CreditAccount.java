package com.ironhack.ironbank.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ironhack.ironbank.helpclasses.AccountStatus;
import com.ironhack.ironbank.helpclasses.Money;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
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
    @Type(type = "org.hibernate.type.UUIDCharType")
    UUID accountID;

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "org.hibernate.type.UUIDCharType")
    UUID secretKey;

    @ManyToOne
    @JoinTable(
            name = "account_holder_primary_credit_accounts",
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

    LocalDate creationDate;

    BigDecimal creditLimit;

    BigDecimal interestRate;

    @Enumerated(EnumType.STRING)
    AccountStatus accountStatus;

//    @OneToMany
//    List<Transaction> transactionList;

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
        return "Credit Account - " + "accountID: " + accountID + "\n"+
                "secretKey: " + secretKey + "\n"+
                "primaryOwner: \n" +
                "  Owner ID: "+ primaryOwner.getId() + "\n" +
                "  Owner Name: "+ primaryOwner.getFirstname() +" "+ primaryOwner.getLastname()+ "\n" +
                secondaryToString + "\n"+
                "balance: " + balance + "\n"+
                "creationDate: " + creationDate + "\n"+
                "creditLimit: " + creditLimit + "\n"+
                "interestRate: " + interestRate + "\n"+
                "accountStatus: " + accountStatus;
    }
}
