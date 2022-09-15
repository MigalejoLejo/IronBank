package com.ironhack.ironbank.users.DTO;

import lombok.Getter;

@Getter
public class TokenRequest {

    String email;
    String password;
}