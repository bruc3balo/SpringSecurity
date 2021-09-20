package com.security.spring.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RoleToUserForm {
    @JsonProperty(value = "username")
    private String username;
    @JsonProperty(value = "name")
    private String roleName;

    public RoleToUserForm() {

    }
}
