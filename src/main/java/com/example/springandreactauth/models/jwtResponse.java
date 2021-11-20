package com.example.springandreactauth.models;

import java.io.Serializable;

public class jwtResponse implements Serializable {

    private final String jwt;

    public jwtResponse(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }
}