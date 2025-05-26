package com.apiautomation.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestLogin {

    /**
     * {
     * "email": "drajattt@gmail.com",
     * "password": "admin123!"
     * }
     */

    @JsonProperty("email")
    public String email;

    @JsonProperty("password")
    public String password;

    public RequestLogin() {
    }

    public RequestLogin(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
