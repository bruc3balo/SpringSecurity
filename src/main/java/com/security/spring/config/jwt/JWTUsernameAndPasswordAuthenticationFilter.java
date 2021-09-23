package com.security.spring.config.jwt;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    //todo userservice

    private final AuthenticationManager authenticationManager;

    public JWTUsernameAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        return TokenHelper.attemptAuthenticationAuthFilter(request,response,authenticationManager); //todo get auth manager
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        TokenHelper.successfulAuthenticationAuthFilter(request, response, chain, authResult);
    }
}
