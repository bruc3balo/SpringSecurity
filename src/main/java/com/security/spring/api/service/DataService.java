package com.security.spring.api.service;

import com.security.spring.api.domain.AppRole;
import com.security.spring.api.domain.AppUser;

import java.util.List;

public interface DataService {

    //User
    AppUser saveAUser(AppUser appUser);
    AppUser getAUser(String username);
    List<AppUser> getAllUsers ();

    //Role
    AppRole saveARole(AppRole appRole);
    AppRole getARole(String name);
    List<AppRole> getAllRoles ();
    void addARoleToAUser(String username, String roleName);
}
