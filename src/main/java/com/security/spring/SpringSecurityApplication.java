package com.security.spring;

import com.security.spring.api.domain.Models;
import com.security.spring.api.model.NewUserForm;
import com.security.spring.api.model.RoleCreationForm;
import com.security.spring.utils.ConvertToJson;
import javassist.NotFoundException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;

import com.security.spring.api.domain.Models.AppRole;
import com.security.spring.api.domain.Models.Permissions;

import com.security.spring.config.security.AppRolesEnum;
import com.security.spring.config.security.AppUserPermission;
import com.security.spring.utils.DataOps;
import javassist.NotFoundException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.security.spring.global.GlobalService.dataService;


@SpringBootApplication
@EnableJpaRepositories
public class SpringSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityApplication.class, args);
    }

    @Bean
    CommandLineRunner run() {
        return args -> {

            //roles
            Set<String> roles = Arrays.stream(AppRolesEnum.values()).map(Enum::name).collect(Collectors.toSet());
            Set<RoleCreationForm> roleCreationFormSet = new HashSet<>();
            final int[] c = {0};
            roles.forEach(r -> {
                Set<String> permissionsList = Enum.valueOf(AppRolesEnum.class, r).getGrantedAuthorities().stream().filter(i -> !Objects.equals(i, DataOps.getGrantedAuthorityRole(r))).map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toSet());
                RoleCreationForm roleCreationForm = new RoleCreationForm(r, permissionsList);
                roleCreationFormSet.add(roleCreationForm);
                System.out.println("form " + Arrays.toString(c) + " "+ConvertToJson.setJsonString(roleCreationForm));
                c[0]++;
            });

            dataService.saveRolesList(roleCreationFormSet);

            //Users
            dataService.saveAUser(new NewUserForm("superadmin", "superadmin", "superadmin@superadmin.com", "superadmin", AppRolesEnum.ROLE_ADMIN.name()));
            dataService.saveAUser(new NewUserForm("admintrainee", "admintrainee", "admintrainee@admintrainee.com", "admintrainee", AppRolesEnum.ROLE_ADMIN_TRAINEE.name()));
            dataService.saveAUser(new NewUserForm("user", "user", "user@user.com", "user", AppRolesEnum.ROLE_USER.name()));


        };
    }

}
