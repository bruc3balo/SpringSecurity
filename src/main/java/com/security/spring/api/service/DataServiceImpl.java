package com.security.spring.api.service;

import com.security.spring.api.domain.AppRole;
import com.security.spring.api.domain.AppUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.security.spring.global.GlobalService.passwordEncoder;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.security.spring.global.GlobalRepositories.roleRepo;
import static com.security.spring.global.GlobalRepositories.userRepo;

@Service
@Transactional
@Slf4j
public class DataServiceImpl implements DataService, UserDetailsService {

    //User
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AppUser appUser = userRepo.findByUsername(username);

        if (appUser == null) {
            log.error("User not found in db");
            throw new UsernameNotFoundException("User not found in db");
        } else {
            log.info("User {} found in db ", appUser.getUsername());
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        appUser.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new User(appUser.getUsername(), appUser.getPassword(), authorities);
    }


    @Override
    public AppUser saveAUser(AppUser appUser) {
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        log.info("Encoding password");
        log.info("Saving new user {} to db", appUser.getUsername());
        return userRepo.save(appUser);
    }

    @Override
    public AppUser getAUser(String username) {
        log.info("Fetching user {} ", username);
        return userRepo.findByUsername(username);
    }

    @Override
    public List<AppUser> getAllUsers() {
        log.info("Fetching all users");
        return userRepo.findAll();
    }

    //Role
    @Override
    public AppRole saveARole(AppRole appRole) {
        log.info("Saving new role {} to db", appRole.getName());
        return roleRepo.save(appRole);
    }

    @Override
    public AppRole getARole(String name) {
        log.info("Fetching role {} ", name);

        return roleRepo.findByName(name);
    }

    @Override
    public List<AppRole> getAllRoles() {
        log.info("Fetching all roles");
        return roleRepo.findAll();
    }

    @Override
    public void addARoleToAUser(String username, String roleName) {
        AppUser user = getAUser(username);
        AppRole role = getARole(roleName);

        log.info("Adding role {} to user {}", user.getUsername(), role.getName());
        user.getRoles().add(role); //will save because @Transactional
    }

}
