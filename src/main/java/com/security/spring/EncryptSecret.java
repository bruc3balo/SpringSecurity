package com.security.spring;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class EncryptSecret {
    public static void main(String[] args) {
        //secret

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        System.out.println(passwordEncoder.encode("secret"));
        System.out.println(passwordEncoder.matches("secret", "$2a$10$l8Ffz3g/z0FHOFj3Op58eOdwK0kEINCgr9bZcrjz65ZJjgTxQCTdO"));
    }
}
