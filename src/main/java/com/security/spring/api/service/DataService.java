package com.security.spring.api.service;


import com.security.spring.api.domain.Models;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Set;

public interface DataService {

    //User
    Models.AppUser saveAUser(Models.AppUser appUser);
    Models.AppUser getAUser(String username);
    Page<Models.AppUser> getAllUsers (Specification<Models.AppUser> specification, PageRequest pageRequest);

    //Role
    Models.AppRole saveARole(Models.AppRole appRole);
    Models.AppRole getARole(String name);
    List<Models.AppRole> getAllRoles ();
    void addARoleToAUser(String username, String roleName) throws NotFoundException;


    //Permission
    Models.Permissions saveAPermission(Models.Permissions permissions);
    Models.Permissions getAPermission(String name);
    List<Models.Permissions> getAllPermissions ();
    void addAPermissionToARole(String roleName, String permissionName) throws NotFoundException;
    void addAPermissionToARole(String roleName, Set<String> permissionName) throws NotFoundException;


}
