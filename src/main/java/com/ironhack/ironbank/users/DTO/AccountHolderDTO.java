package com.ironhack.ironbank.users.DTO;

import com.ironhack.ironbank.http.requests.KeycloakUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountHolderDTO  {
    String username;
    String password;
    String email;
    String firstname;
    String lastname;
    String id;
    Date dateOfBirth;
    String street;
    String number;
    String floor;
    String postalCode;
    String city;
    String land;

    public AccountHolderDTO(String username, String password, String email, String firstname, String lastname, Date dateOfBirth, String street, String number, String floor, String postalCode, String city, String land) {
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

    public void setId(String id) {
        this.id = id;
    }



}
