package com.security.spring.config.jwt;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.security.spring.api.domain.Models;
import com.security.spring.api.model.UsernameAndPasswordAuthenticationRequest;
import com.security.spring.global.GlobalService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.base.Strings;
import com.security.spring.utils.ApiCode;
import com.security.spring.utils.JsonResponse;
import com.security.spring.utils.JsonSetErrorResponse;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.security.spring.global.GlobalVariables.*;
import static java.util.Arrays.stream;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class TokenHelper {

    private static final Algorithm myAlgorithm = Algorithm.HMAC256(secretJwt.getBytes());

    //Mostly Used
    public static Authentication attemptAuthenticationAuthFilter(HttpServletRequest request, HttpServletResponse response, AuthenticationManager authenticationManager) {
        try {
            UsernameAndPasswordAuthenticationRequest authenticationRequest = new ObjectMapper().readValue(request.getInputStream(), UsernameAndPasswordAuthenticationRequest.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword());

            return authenticationManager.authenticate(authentication);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void successfulAuthenticationAuthFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, Authentication authResult) throws IOException {
        response.setContentType(APPLICATION_JSON_VALUE);

        System.out.println("Authorities are " + authResult.getAuthorities());

        String accessToken = JWT.create().withSubject(authResult.getName()).withExpiresAt(accessTokenTIme).withIssuer(request.getRequestURL().toString()).withClaim("authorities", authResult.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())).sign(myAlgorithm);

        String bearerToken = "Bearer " + accessToken;

        response.addHeader("Authorization", bearerToken);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", bearerToken);

        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }

    public static void unsuccessfulAuthenticationAuthFilter(HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(FORBIDDEN.value());
        JsonResponse failResponse = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), failed.getMessage(), "");
        new ObjectMapper().writeValue(response.getOutputStream(), failResponse);
    }

    public static void JwtTokenVerify(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader(jwtAuthHeader);

        if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(tokenPrefix)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.replace(tokenPrefix, "");

        JWTVerifier verifier = JWT.require(myAlgorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);

        String username = decodedJWT.getSubject();
        String[] roles = decodedJWT.getClaim("authorities").asArray(String.class);

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response); //pass on to next filter
    }

    public static void failedJwtVerify(HttpServletResponse response, Exception e) throws IOException {

        response.setStatus(FORBIDDEN.value());

        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }

    public static void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(jwtAuthHeader);
        response.setContentType(APPLICATION_JSON_VALUE);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Refresh token is missing");
        }


        try {
            String refresh_token = authorizationHeader.substring("Bearer ".length());
            JWTVerifier verifier = JWT.require(myAlgorithm).build();
            DecodedJWT decodedJWT = verifier.verify(refresh_token);

            String username = decodedJWT.getSubject();
            Models.AppUser user = GlobalService.dataService.getAUser(username);

            String access_token = JWT.create().withSubject(user.getUsername()).withExpiresAt(refreshTokenTIme).withIssuer(request.getRequestURL().toString()).withClaim("authorities", user.getRoles().stream().map(Models.AppRole::getName).collect(Collectors.toList())).sign(myAlgorithm);

            Map<String, String> tokens = new HashMap<>();
            tokens.put("access_token", access_token);
            //tokens.put("refresh_token", refresh_token); todo to put or not to put

            new ObjectMapper().writeValue(response.getOutputStream(), tokens);

        } catch (Exception e) {
            e.printStackTrace();

            log.error("Error logging in : " + e.getMessage());

            response.setHeader("error", e.getMessage());
            response.setStatus(FORBIDDEN.value());

            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());

            new ObjectMapper().writeValue(response.getOutputStream(), error);
        }

    }


   /* //Not Used
    public static void accessToken(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if (request.getServletPath().equals("/api/v1/login") || request.getServletPath().equals("/api/v1/user/token/refresh")) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(jwtAuthHeader);
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                try {
                    String token = authorizationHeader.substring("Bearer ".length());
                    JWTVerifier verifier = JWT.require(myAlgorithm).build();
                    DecodedJWT decodedJWT = verifier.verify(token);

                    String username = decodedJWT.getSubject();
                    String[] roles = decodedJWT.getClaim("authorities").asArray(String.class);
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));

                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                    filterChain.doFilter(request, response);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Error logging in : " + e.getMessage());
                    response.setHeader("error", e.getMessage());
                    response.setStatus(FORBIDDEN.value());

                    Map<String, String> error = new HashMap<>();
                    error.put("error", e.getMessage());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                }

            } else {
                filterChain.doFilter(request, response);
            }
        }
    }

    public static void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, Authentication authResult) throws IOException {
        User user = (User) authResult.getPrincipal();
        String accessToken = JWT.create().withSubject(user.getUsername()).withExpiresAt(accessTokenTIme).withIssuer(request.getRequestURL().toString()).withClaim("authorities", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())).sign(myAlgorithm);
        String refreshToken = JWT.create().withSubject(user.getUsername()).withExpiresAt(GlobalVariables.refreshTokenTIme).withIssuer(request.getRequestURL().toString()).withClaim("authorities", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())).sign(myAlgorithm);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }

    public static Authentication attemptAuthentication(HttpServletRequest request, AuthenticationManager authenticationManager) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        log.info("Username is {} , password is {}", username, password);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }*/
}
