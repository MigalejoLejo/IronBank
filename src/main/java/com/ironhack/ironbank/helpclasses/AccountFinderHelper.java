package com.ironhack.ironbank.helpclasses;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class AccountFinderHelper {

    UUID accountID;
    TypeOfAccount typeOfAccount;


}
