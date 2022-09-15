package com.ironhack.ironbank.users.DTO;


import com.ironhack.ironbank.users.DTO.AccountHolderDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Inheritance;
import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@Inheritance
public class KeycloakUser {
    String id;
    String username;
    String password;
    String email;
    String firstname;
    String lastname;
    Date dateOfBirth;
    String street;
    String number;
    String floor;
    String postalCode;
    String city;
    String land;

    public KeycloakUser(String username, String password, String email ) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public KeycloakUser(String username, String password, String email, String firstname, String lastname, Date dateOfBirth, String street, String number, String floor, String postalCode, String city, String land) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.dateOfBirth = dateOfBirth;
        this.street = street;
        this.number = number;
        this.floor = floor;
        this.postalCode = postalCode;
        this.city = city;
        this.land = land;
    }
}
