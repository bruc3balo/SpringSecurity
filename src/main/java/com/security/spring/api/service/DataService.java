package com.security.spring.api.service;


import com.security.spring.api.domain.Models;

import com.security.spring.api.model.NewUserForm;
import com.security.spring.api.model.RoleCreationForm;
import com.security.spring.api.model.UserUpdateForm;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.text.ParseException;
import java.util.List;
import java.util.Set;

public interface DataService {

    //User
    Models.AppUser saveAUser(NewUserForm userForm) throws NotFoundException, ParseException; //working
    Models.AppUser updateAUser(String username, UserUpdateForm updateForm) throws NotFoundException, ParseException; //working
    Models.AppUser toggleUserDeletion(Models.AppUser user) throws NotFoundException; //works
    Models.AppUser getAUser(String username); //works
    Page<Models.AppUser> getAllUsers(Specification<Models.AppUser> specification, PageRequest pageRequest); //works
    Page<Models.AppUser> getAllUsers(PageRequest pageRequest); //works

    //Role
   // Models.AppRole saveARole(String name) throws NotFoundException; //works
    Models.AppRole saveANewRole(RoleCreationForm form) throws NotFoundException;
    Set<Models.AppRole> saveRolesList(Set<RoleCreationForm> creationForms); //works
    Models.AppRole getARole(String name); //works
    List<Models.AppRole> getAllRoles(); //works
    void addARoleToAUser(String username, String roleName) throws NotFoundException; //working


    //Permission
    Models.Permissions saveAPermission(Models.Permissions permissions); //works
    Models.Permissions getAPermission(String name); //works
    List<Models.Permissions> getAllPermissions(); //works
    Models.AppRole addAPermissionToARole(String roleName, String permissionName) throws NotFoundException; //works
    Models.AppRole addPermissionListToARole(String roleName, Set<String> permissionName) throws NotFoundException; //works


}
