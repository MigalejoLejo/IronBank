package com.ironhack.ironbank.http.requests;


import com.ironhack.ironbank.users.DTO.AccountHolderDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Inheritance;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Inheritance
public class KeycloakUser {
    String username;
    String password;
    String email;
    String firstname;
    String lastname;



}
