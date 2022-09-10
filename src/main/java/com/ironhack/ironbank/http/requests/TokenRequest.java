package com.ironhack.ironbank.http.requests;

import lombok.Getter;

@Getter
public class TokenRequest {

    String email;
    String password;
}