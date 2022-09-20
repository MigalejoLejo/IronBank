package com.ironhack.ironbank.model;

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
public class Savings {

    final BigDecimal MINIMUM_BALANCE = new BigDecimal("1000");

    final BigDecimal PENALTY_FEE = new BigDecimal("40");
    final BigDecimal MONTHLY_MAINTENANCE_FEE = new BigDecimal("0");

    final BigDecimal MAX_INTEREST_RATE = new BigDecimal("0.5");
    final BigDecimal DEFAULT_INTEREST_RATE = new BigDecimal("0.0025");

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID accountID;

    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID secretKey;

    @ManyToOne
    AccountHolder primaryOwner;

    @ManyToOne
    AccountHolder secondaryOwner;

    Money balance;

    Instant creationDate;

    BigDecimal interestRate;

    @Enumerated(EnumType.STRING)
    AccountStatus accountStatus;
}
