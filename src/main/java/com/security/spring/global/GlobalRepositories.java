package com.security.spring.global;

import com.security.spring.api.repository.AppRoleRepo;
import com.security.spring.api.repository.AppUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GlobalRepositories {

    public static AppUserRepo userRepo;
    public static AppRoleRepo roleRepo;


    @Autowired
    public void setAppUserRepo(AppUserRepo appUserRepo) {
        GlobalRepositories.userRepo = appUserRepo;
    }

    @Autowired
    public void setAppRoleRepo(AppRoleRepo appRoleRepo) {
        GlobalRepositories.roleRepo = appRoleRepo;
    }
}
