package com.ironhack.ironbank.DTO;

import com.ironhack.ironbank.helpclasses.AccountStatus;
import com.ironhack.ironbank.model.Checking;
import com.ironhack.ironbank.model.CreditAccount;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
public class CreditDTO {

    UUID accountID;
    UUID secretKey;

    String primaryOwnerId;
    String secondaryOwnerId;

    String balance;
    String creditLimit;
    String interestRate;

    LocalDate creationDate;
    AccountStatus accountStatus;


    public static CreditDTO fromEntity (CreditAccount account){
        return new CreditDTO(
                account.getAccountID(),
                account.getSecretKey(),
                account.getPrimaryOwner().getId(),
                account.getSecondaryOwner() == null? account.getSecondaryOwner().getId():null,
                account.getBalance().getAmount().toString(),
                account.getCreditLimit().toString(),
                account.getInterestRate().toString(),
                account.getCreationDate(),
                account.getAccountStatus()
        );
    }
}
