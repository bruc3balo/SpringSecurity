package com.security.spring.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;


@Entity
@Table(name = "users")
@Getter
@Setter
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name")
    @JsonProperty(value = "name")
    private String name;

    @Column(name = "username")
    @JsonProperty(value = "username")
    private String username;

    @Column(name = "email_address")
    @JsonProperty(value = "email_address")
    private String emailAddress;

    @Column(name = "password")
    @JsonProperty(value = "password")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER) //anytime load user, load a role
    private Collection<AppRole> roles = new ArrayList<>();

    public AppUser() {

    }

    public AppUser(String name, String username, String emailAddress, String password, Collection<AppRole> roles) {
        this.name = name;
        this.username = username;
        this.emailAddress = emailAddress;
        this.password = password;
        this.roles = roles;
    }
}
