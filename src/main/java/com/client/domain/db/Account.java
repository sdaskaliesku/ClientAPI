package com.client.domain.db;

import javax.persistence.*;

import com.client.domain.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
@Entity
@Table(name = "Account")
@NamedQuery(name = Account.FIND_BY_NAME, query = "select a from Account a where a.name = :name")
public class Account implements java.io.Serializable {

    public static final String FIND_BY_NAME = "Account.findByName";

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String name;

    @JsonIgnore
    private String password;

    private Role role = Role.ROLE_ADMIN;

    public Account() {

    }

    public Account(String name, String password, Role role) {
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
