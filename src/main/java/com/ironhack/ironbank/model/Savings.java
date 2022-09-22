package com.ironhack.ironbank.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ironhack.ironbank.helpclasses.AccountStatus;
import com.ironhack.ironbank.helpclasses.Money;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Savings {

    public final static BigDecimal MINIMUM_BALANCE = new BigDecimal("1000");

    public final static BigDecimal PENALTY_FEE = new BigDecimal("40");
    public final static BigDecimal MONTHLY_MAINTENANCE_FEE = new BigDecimal("0");

    public final static BigDecimal MAX_INTEREST_RATE = new BigDecimal("0.5");
    public final static BigDecimal DEFAULT_INTEREST_RATE = new BigDecimal("0.0025");

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID accountID;

    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID secretKey;

    @ManyToOne
    @JoinTable(
            name = "account_holder_primaary_savings",
            joinColumns = @JoinColumn(name = "account_holder_id"),
            inverseJoinColumns = @JoinColumn(name = "savings_id")
    )
    @JsonIgnore
    AccountHolder primaryOwner;

    @ManyToOne
    @JoinTable(
            name = "account_holder_secondary_savings",
            joinColumns = @JoinColumn(name = "account_holder_id"),
            inverseJoinColumns = @JoinColumn(name = "savings_id")
    )
    @JsonIgnore
    AccountHolder secondaryOwner;

    Money balance;

    LocalDate creationDate;

    BigDecimal interestRate;

    @Enumerated(EnumType.STRING)
    AccountStatus accountStatus;


    public Savings(AccountHolder primaryOwner, AccountHolder secondaryOwner, Money balance, BigDecimal interestRate) {
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        this.balance = balance;
        this.interestRate = interestRate;
    }

    public static Savings createAccount(AccountHolder primaryOwner, AccountHolder secondaryOwner, Money balance, BigDecimal interestRate) {
        Savings newAccount = new Savings(primaryOwner, secondaryOwner, balance, interestRate);
        newAccount.secretKey = UUID.randomUUID();
        newAccount.creationDate = LocalDate.now();
        newAccount.accountStatus = AccountStatus.OPEN;
        return newAccount;
    }

    @Override
    public String toString() {
        return "Savings Account - " + "accountID: " + accountID + "\n" +
                "secretKey: " + secretKey + "\n" +
                "primaryOwner: \n" +
                "  Owner ID: " + primaryOwner.getId() + "\n" +
                "  Owner Name: " + primaryOwner.getFirstname() + " " + primaryOwner.getLastname() + "\n" +
                "secondaryOwner: \n" +
                "  Owner ID: " + secondaryOwner.getId() + "\n" +
                "  Owner Name: " + secondaryOwner.getFirstname() + " " + secondaryOwner.getLastname() + "\n" +
                "balance: " + balance + "\n" +
                "creationDate: " + creationDate + "\n" +
                "interestRate: " + interestRate + "\n" +
                "accountStatus: " + accountStatus;
    }
}
