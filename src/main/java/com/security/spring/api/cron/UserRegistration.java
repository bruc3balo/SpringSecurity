package com.security.spring.api.cron;


import com.security.spring.api.domain.Models;
import com.security.spring.api.service.DataService;
import com.security.spring.config.security.AppRolesEnum;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;

import static com.security.spring.global.GlobalService.dataService;
import static com.security.spring.utils.DataOps.getNowFormattedFullDate;


@Slf4j
@Component
public class UserRegistration {

    @Scheduled(fixedDelay = 5000, initialDelay = 10000) //every 10 seconds
    private void addRolesToUsers() {

        Page<Models.AppUser> usersWithoutRoles = dataService.getAllUsers(PageRequest.of(0, Integer.MAX_VALUE));
        if (!usersWithoutRoles.isEmpty()) {
            //log.info("Checking user roles");
            usersWithoutRoles.forEach(u -> {
                if (u.getRole() == null) {
                    Models.AppRole role = dataService.getARole(AppRolesEnum.ROLE_USER.name());
                    if (role != null) {
                        try {
                            u.setUpdatedAt(getNowFormattedFullDate());
                            dataService.addARoleToAUser(u.getUsername(), role.getName());
                        } catch (ParseException | NotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }
}
