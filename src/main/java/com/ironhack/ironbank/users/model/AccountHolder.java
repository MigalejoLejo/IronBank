package com.ironhack.ironbank.users.model;

import com.ironhack.ironbank.users.DTO.AccountHolderDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;

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
    String firstname;
    String lastname;
    Date dateOfBirth;
    @Embedded
    Address address;


    public AccountHolder(String username, String email, String firstname, String lastname, Date dateOfBirth, Address address) {
        this.username = username;
        this.email = email;
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
