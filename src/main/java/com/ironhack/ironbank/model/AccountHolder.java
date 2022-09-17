package com.ironhack.ironbank.model;

import com.ironhack.ironbank.DTO.KeycloakUser;
import com.ironhack.ironbank.DTO.AccountHolderDTO;
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
    String password;
    String firstname;
    String lastname;
    Date dateOfBirth;
    @Embedded
    Address address;


    public AccountHolder(String username, String email, String password, String firstname, String lastname, Date dateOfBirth, Address address) {
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

    public static AccountHolder fromKeycloakUser(KeycloakUser user){
        return new AccountHolder(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getFirstname(),
                user.getLastname(),
                user.getDateOfBirth(),
                new Address(
                        user.getStreet(),
                        user.getNumber(),
                        user.getFloor(),
                        user.getPostalCode(),
                        user.getCity(),
                        user.getLand()
                )
        );
    }
}
