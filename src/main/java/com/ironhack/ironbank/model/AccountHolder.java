package com.ironhack.ironbank.model;

import com.ironhack.ironbank.DTO.AccountHolderDTO;
import com.ironhack.ironbank.helpclasses.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountHolder{

    @Id
    String id;
    String username;
    String email;
    String password;
    String firstname;
    String lastname;
    LocalDate dateOfBirth;
    @Embedded
    Address address;

    @OneToMany(mappedBy = "primaryOwner")
    List<Checking> checkingAccounts;

    @OneToMany(mappedBy = "primaryOwner")
    List<StudentChecking> studentCheckingAccounts;


    public AccountHolder(String id, String username, String email, String password, String firstname, String lastname, LocalDate dateOfBirth, Address address) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public static AccountHolder fromDTO(AccountHolderDTO accountHolderDTO){
        return new AccountHolder(
            accountHolderDTO.getId(),
            accountHolderDTO.getUsername(),
            accountHolderDTO.getEmail(),
            accountHolderDTO.getPassword(),
            accountHolderDTO.getFirstname(),
            accountHolderDTO.getLastname(),
            accountHolderDTO.getDateOfBirth(),
            new Address(
                accountHolderDTO.getStreet(),
                accountHolderDTO.getNumber(),
                accountHolderDTO.getFloor(),
                accountHolderDTO.getPostalCode(),
                accountHolderDTO.getCity(),
                accountHolderDTO.getLand()
            )
        );
    }

}
