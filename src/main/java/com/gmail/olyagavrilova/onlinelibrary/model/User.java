package com.gmail.olyagavrilova.onlinelibrary.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "users")
public class User {

    @Id
    private int id;

    private String login;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
    private Boolean blocked = Boolean.FALSE;

    public User() {
    }

    public User(int id, String login, String password, Role role) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public User(String login, String password, Role role, boolean blocked) {
        this.login = login;
        this.password = password;
        this.role = role;
        this.blocked = blocked;
    }

    public User(String login, String password, Role role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }
}
