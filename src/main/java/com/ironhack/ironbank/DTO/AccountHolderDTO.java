package com.ironhack.ironbank.DTO;

import com.ironhack.ironbank.model.AccountHolder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountHolderDTO {
    String id;
    String username;
    String password;
    String email;
    String firstname;
    String lastname;
    LocalDate dateOfBirth;
    String street;
    String number;
    String floor;
    String postalCode;
    String city;
    String land;

    public static AccountHolderDTO fromEntity(AccountHolder accountHolder) {
        return new AccountHolderDTO(
                accountHolder.getId(),
                accountHolder.getUsername(),
                accountHolder.getEmail(),
                accountHolder.getPassword(),
                accountHolder.getFirstname(),
                accountHolder.getLastname(),
                accountHolder.getDateOfBirth(),
                accountHolder.getAddress().getStreet(),
                accountHolder.getAddress().getNumber(),
                accountHolder.getAddress().getFloor(),
                accountHolder.getAddress().getPostalCode(),
                accountHolder.getAddress().getCity(),
                accountHolder.getAddress().getLand()
        );
    }
}
