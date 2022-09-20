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
@Setter
@Getter
@NoArgsConstructor
public class CreditAccount{

    final BigDecimal PENALTY_FEE = new BigDecimal("40");

    final BigDecimal MAX_CREDIT_LIMIT = new BigDecimal("100000");
    final BigDecimal DEFAULT_CREDIT_LIMIT = new BigDecimal("100");

    final BigDecimal MIN_INTEREST_RATE = new BigDecimal("0.1");
    final BigDecimal DEFAULT_INTEREST_RATE = new BigDecimal("0.2");

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

    BigDecimal creditLimit;

    BigDecimal interestRate;

    @Enumerated(EnumType.STRING)
    AccountStatus accountStatus;
}
