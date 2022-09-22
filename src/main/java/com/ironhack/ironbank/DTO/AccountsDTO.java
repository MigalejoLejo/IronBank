package com.ironhack.ironbank.DTO;



import com.ironhack.ironbank.model.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountsDTO {

    List<Checking> primaryCheckings;
    List<Checking> secondaryCheckings;
    List<StudentChecking> primaryStudentCheckings;
    List<StudentChecking> secondaryStudentCheckings;
    List<CreditAccount> primaryCreditAccounts;
    List<CreditAccount> secondaryCreditAccounts;
    List<Savings> primarySavings;
    List<Savings> secondarySavings;

    public static AccountsDTO fromAccountHolder(AccountHolder user) {
        return new AccountsDTO(
                user.getPrimaryCheckings(),
                user.getSecondaryCheckings(),
                user.getPrimaryStudentCheckings(),
                user.getSecondaryStudentCheckings(),
                user.getPrimaryCreditAccounts(),
                user.getSecondaryCreditAccounts(),
                user.getPrimarySavings(),
                user.getSecondarySavings()
        );
    }

}
