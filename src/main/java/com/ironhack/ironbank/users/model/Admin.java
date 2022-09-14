package com.ironhack.ironbank.users.model;

import com.ironhack.ironbank.http.requests.KeycloakUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Admin  {

    @Id
    String id;
    String username;
    String email;
    String firstname;
    String lastname;



}
