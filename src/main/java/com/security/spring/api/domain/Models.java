package com.security.spring.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class Models {

    @Entity
    @Table(name = "users")
    @Getter
    @Setter
    public static class AppUser {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "name")
        @JsonProperty(value = "name")
        private String name;

        @Column(name = "username", unique = true)
        @JsonProperty(value = "username")
        private String username;

        @Column(name = "email_address")
        @JsonProperty(value = "email_address")
        private String emailAddress;

        @Column(name = "password")
        @JsonProperty(value = "password")
        private String password;

        @Column(updatable = false, name = "created_at")
        private Date createdAt;

        @Column(name = "deleted")
        private boolean deleted;

        @ManyToMany(fetch = FetchType.EAGER) //anytime load user, load a role
        private Collection<AppRole> roles = new ArrayList<>();

        public AppUser() {

        }

        public AppUser(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public AppUser(String name) {
            this.name = name;
        }


        public AppUser(String name, String username, String emailAddress, String password, boolean deleted, Collection<AppRole> roles) {
            this.name = name;
            this.username = username;
            this.emailAddress = emailAddress;
            this.password = password;
            this.roles = roles;
            this.deleted = deleted;
        }
    }

    @Entity
    @Table(name = "roles")
    @Getter
    @Setter
    @AllArgsConstructor
    public static class AppRole{

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "name")
        @JsonProperty(value = "name")
        private String name;


        @ManyToMany(fetch = FetchType.EAGER)
        @Column(name = "permissions")
        private Set<Permissions> allowedPermissions = new HashSet<>();

        public AppRole() {

        }

        public AppRole(String name) {
            this.name = name;
        }



        private Collection<GrantedAuthority> getGrantedAllowedPermissions() {
            return allowedPermissions.stream().map(permissions -> new SimpleGrantedAuthority(permissions.getName())).collect(Collectors.toSet());
        }
    }


    @Entity
    @Table(name = "permissions")
    @Getter
    @Setter
    public static class Permissions{
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "name")
        private String name;

        public Permissions() {
        }

        public Permissions(String name) {
            this.name = name;
        }



    }


}

