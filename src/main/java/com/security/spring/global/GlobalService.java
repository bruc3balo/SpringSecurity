package com.security.spring.global;

import com.security.spring.api.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class GlobalService {

    public static DataService dataService;
    public static UserDetailsService userDetailsService;
    public static BCryptPasswordEncoder passwordEncoder;


    @Autowired
    public void setDataService(DataService dataService) {
        GlobalService.dataService = dataService;
    }

    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        GlobalService.userDetailsService = userDetailsService;
    }

    @Autowired
    public void setPasswordEncoder() {
        GlobalService.passwordEncoder = new BCryptPasswordEncoder();
    }


}
