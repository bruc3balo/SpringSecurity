package com.security.spring.api.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.security.spring.api.domain.AppRole;
import com.security.spring.api.domain.AppUser;
import com.security.spring.api.model.RoleToUserForm;
import com.security.spring.utils.TokenHelper;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.security.spring.global.GlobalService.dataService;
import static com.security.spring.global.GlobalService.passwordEncoder;
import static com.security.spring.global.GlobalVariables.accessTokenTIme;
import static com.security.spring.global.GlobalVariables.myAlgorithm;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @GetMapping("/all")
    public ResponseEntity<List<AppUser>> getAllUsers() {
        return ResponseEntity.ok().body(dataService.getAllUsers());
    }

    @PostMapping("/save")
    public ResponseEntity<AppUser> saveUser(@RequestBody AppUser appUser) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/save").toUriString());
        log.info("uri saveuser ::: {}", uri);
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        return ResponseEntity.created(uri).body(dataService.saveAUser(appUser));
    }

    @PostMapping("/saveRole")
    public ResponseEntity<AppRole> saveRole(@RequestBody AppRole appRole) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/saveRole").toUriString());
        log.info("uri saverole ::: {}", uri);

        return ResponseEntity.created(uri).body(dataService.saveARole(appRole));
    }

    @PostMapping("/addroletouser")
    public ResponseEntity<AppRole> addRoleToUser(@RequestBody RoleToUserForm form) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/addroletouser").toUriString());
        log.info("uri addroletouser ::: {}", uri);

        dataService.addARoleToAUser(form.getUsername(), form.getRoleName());

        return ResponseEntity.created(uri).build();
    }

    @GetMapping(value = {"/token/refresh"})
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        TokenHelper.refreshToken(request, response);
    }
}

