package com.security.spring;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class EncryptSecret {
    public static void main(String[] args) {
            //secret
         System.out.println(new BCryptPasswordEncoder(10).matches("secret","$2a$10$l8Ffz3g/z0FHOFj3Op58eOdwK0kEINCgr9bZcrjz65ZJjgTxQCTdO"));;
    }
}
