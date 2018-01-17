package com.client.domain;

import com.client.domain.db.Account;
import com.client.domain.enums.Role;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class SignupRequest {

    private static final String NOT_BLANK_MESSAGE = "{notBlank.message}";

    @NotBlank(message = SignupRequest.NOT_BLANK_MESSAGE)
    @Length(min = 3, max = 16)
    private String name;

    @NotBlank(message = SignupRequest.NOT_BLANK_MESSAGE)
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Account createAccount() {
        return new Account(getName(), getPassword(), Role.ROLE_NOTHING);
    }
}
