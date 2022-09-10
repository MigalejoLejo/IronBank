package com.ironhack.ironbank.http.requests;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeycloakUser {
    String username;
    String password;
    String email;
    String firstname;
    String lastname;
}
