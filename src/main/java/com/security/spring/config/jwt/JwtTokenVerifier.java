package com.security.spring.config.jwt;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.security.spring.config.jwt.TokenHelper.JwtTokenVerify;
import static com.security.spring.config.jwt.TokenHelper.failedJwtVerify;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class JwtTokenVerifier extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setContentType(APPLICATION_JSON_VALUE);

        try {
            JwtTokenVerify(request, response, filterChain);
        } catch (Exception e) {
            failedJwtVerify(response, e);
        }
    }


}
