package com.security.spring.api.service;


import com.security.spring.api.domain.Models;
import com.security.spring.api.specification.UserPredicate;
import com.security.spring.global.GlobalRepositories;
import com.sun.jdi.request.DuplicateRequestException;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

import static com.security.spring.global.GlobalRepositories.appPermissionRepo;
import static com.security.spring.global.GlobalRepositories.userRepo;

@Service
@Transactional
@Slf4j
public class DataServiceImpl implements DataService, UserDetailsService {

    //User
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        Models.AppUser appUser = userRepo.findByUsername(username);

        if (appUser == null) {
            log.error("User not found in db");
            throw new UsernameNotFoundException("User not found in db");
        } else {
            log.info("User {} found in db ", appUser.getUsername());
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        appUser.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        appUser.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
            role.getAllowedPermissions().forEach(p -> {
                if (!authorities.contains(new SimpleGrantedAuthority(p.getName()))) {
                    log.info("Adding permissions {} to role {}", role.getName(), p);
                    authorities.add(new SimpleGrantedAuthority(p.getName()));
                } else {
                    System.out.println("Role already has permission");
                }
            });
        });
        return new User(appUser.getUsername(), appUser.getPassword(), authorities);
    }


    @Override
    public Models.AppUser saveAUser(Models.AppUser appUser) {


        if (getAllUsers(new UserPredicate(appUser.getUsername()), PageRequest.of(0, 1)).getContent().contains(appUser)) {
            throw new DuplicateRequestException("User has already been created");
        }

        appUser.setPassword(new BCryptPasswordEncoder(10).encode(appUser.getPassword()));
        appUser.setCreatedAt(new Date());
        log.info("Saving new user {} to db", appUser.getUsername());
        return userRepo.save(appUser);
    }

    @Override
    public Models.AppUser getAUser(String username) {
        log.info("Fetching user {} ", username);
        return userRepo.findByUsername(username);
    }


    @Override
    public Page<Models.AppUser> getAllUsers(Specification<Models.AppUser> specification, PageRequest pageRequest) {
        log.info("Fetching all users");
        return userRepo.findAll(specification, pageRequest);
    }

    //Role
    @Override
    public Models.AppRole saveARole(Models.AppRole appRole) {
        log.info("Saving new role {} to db", appRole.getName());

        if (getAllRoles().contains(appRole)) {
            throw new DuplicateRequestException("Role has already been created");
        }

        return GlobalRepositories.roleRepo.save(appRole);
    }

    @Override
    public Models.AppRole getARole(String name) {
        log.info("Fetching role {} ", name);

        return GlobalRepositories.roleRepo.findByName(name);
    }

    @Override
    public List<Models.AppRole> getAllRoles() {
        log.info("Fetching all roles");
        return GlobalRepositories.roleRepo.findAll();
    }

    @Override
    public void addARoleToAUser(String username, String roleName) throws NotFoundException {
        Models.AppUser user = getAUser(username);
        Models.AppRole role = getARole(roleName);

        if (user != null) {

            if (role != null) {

                if (!user.getRoles().contains(role)) {
                    log.info("Adding role {} to user {}", user.getUsername(), role.getName());
                    user.getRoles().add(role); //will save because @Transactional
                } else {
                    throw new DuplicateRequestException("User already has role " + role);
                }
            } else {
                throw new NotFoundException("Role not found " + roleName);
            }
        } else {
            throw new UsernameNotFoundException("User not found " + username);
        }
    }

    @Override
    public Models.Permissions saveAPermission(Models.Permissions permissions) {
        return appPermissionRepo.save(permissions);
    }

    @Override
    public void addAPermissionToARole(String roleName, Set<String> permissionList) throws NotFoundException {
        Models.AppRole role = getARole(roleName);
        Set<Models.Permissions> permissions = getAllPermissions().stream().filter(p -> permissionList.contains(p.getName())).collect(Collectors.toSet()); //permissions to add from db remove the ones // match names

        if (role != null) {
            role.getAllowedPermissions().addAll(permissions);
        } else {
            throw new NotFoundException("Role not found " + roleName);
        }

    }

    @Override
    public void addAPermissionToARole(String roleName, String permissionName) throws NotFoundException {
        Models.AppRole role = getARole(roleName);
        Models.Permissions permissions = getAPermission(permissionName);

        if (role != null) {
            if (permissions != null) {
                if (!role.getAllowedPermissions().contains(permissions)) {
                    log.info("Adding permission {} to role {}", permissions.getName(), role.getName());
                    role.getAllowedPermissions().add(permissions); //will save because @Transactional
                } else {
                    throw new DuplicateRequestException("Role " + roleName + " already has permission " + permissionName);
                }
            } else {
                throw new NotFoundException("Permission not found " + permissionName + " for role " + roleName);
            }
        } else {
            throw new NotFoundException("Role not found " + roleName);
        }
    }

    @Override
    public List<Models.Permissions> getAllPermissions() {
        return appPermissionRepo.findAll();
    }

    @Override
    public Models.Permissions getAPermission(String name) {
        return appPermissionRepo.findByName(name);
    }
}
