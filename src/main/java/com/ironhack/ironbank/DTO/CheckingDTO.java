package com.ironhack.ironbank.DTO;

import com.ironhack.ironbank.helpclasses.AccountStatus;
import com.ironhack.ironbank.model.Checking;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class CheckingDTO {

    UUID accountID;
    UUID secretKey;

    String primaryOwnerId;
    String secondaryOwnerId;

    String balance;
    LocalDate creationDate;

    AccountStatus accountStatus;


    public static CheckingDTO fromEntity (Checking account){
        return new CheckingDTO(
                account.getAccountID(),
                account.getSecretKey(),
                account.getPrimaryOwner().getId(),
                account.getSecondaryOwner() != null? account.getSecondaryOwner().getId():null,
                account.getBalance().getAmount().toString(),
                account.getCreationDate(),
                account.getAccountStatus()
        );
    }

}
