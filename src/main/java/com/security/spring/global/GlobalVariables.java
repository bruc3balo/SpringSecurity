package com.security.spring.global;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class GlobalVariables {

    public static Algorithm myAlgorithm;
    public static Date accessTokenTIme;
    public static Date refreshTokenTIme;
    public static String contextPath;

    @Autowired
    public void setMyAlgorithm() {
        //todo encrypt the secret
        GlobalVariables.myAlgorithm = Algorithm.HMAC256("secret".getBytes());
    }

    @Autowired
    public void setExpirationDate() {
        GlobalVariables.accessTokenTIme = new Date(System.currentTimeMillis() + 10 * 60 * 1000);
    }

    @Autowired
    public void setRefreshTokenTIme() {
        GlobalVariables.refreshTokenTIme = new Date(System.currentTimeMillis() + 30 * 60 * 1000);
    }

    @Value("${server.servlet.context-path}")
    public void setContextPath(String contextPath) {
        GlobalVariables.contextPath = contextPath;
    }
}
