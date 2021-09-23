package com.security.spring.api.controller;

import com.security.spring.api.domain.Models;
import com.security.spring.api.model.RoleToUserForm;
import com.security.spring.api.specification.UserPredicate;
import com.security.spring.config.jwt.TokenHelper;
import com.security.spring.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.security.spring.global.GlobalService.dataService;
import static com.security.spring.utils.DataOps.filterRequestParams;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<?> getAllUsers(HttpServletRequest request,
                                         @RequestParam(value = "name", required = false) String name,
                                         @RequestParam(value = "username", required = false) String username,
                                         @RequestParam(value = "id", required = false) Long id) {

        log.info("Get users");


        try {
            List<String> unknownParams = filterRequestParams(request, Arrays.asList("name", "id","username"));
            if (!unknownParams.isEmpty()) {
                // get all errors
                String apiDesc = unknownParams.stream().map(x -> "'" + x.toUpperCase() + "'").collect(Collectors.joining(", ")) + " : Not valid Parameters";
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), apiDesc, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            Page<Models.AppUser> userList = dataService.getAllUsers(new UserPredicate(id, name, username), PageRequest.of(0, Integer.MAX_VALUE));

            userList.forEach(u -> u.getRoles().forEach(r -> r.setAllowedPermissions(r.getAllowedPermissions().stream().sorted(Comparator.comparing(Models.Permissions::getId)).collect(Collectors.toCollection(LinkedHashSet::new)))));


            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getDescription(), null, userList.getContent());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<?> saveUser(@RequestBody Models.AppUser appUser) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/save").toUriString());
        log.info("uri saveuser ::: {}", uri);

        try {
            Models.AppUser savedUser = dataService.saveAUser(appUser);
            JsonResponse response = JsonSetSuccessResponse.setResponse(savedUser != null ? ApiCode.SUCCESS.getCode() : ApiCode.FAILED.getCode(), savedUser != null ? ApiCode.SUCCESS.getDescription() : ApiCode.FAILED.getDescription(), null, savedUser);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "update/{id}")
    @PreAuthorize("hasAuthority('user:update')")
    public ResponseEntity<?> updateUser(HttpServletRequest request,
                                        @RequestParam(value = "name", required = false) String name,
                                        @RequestParam(value = "username", required = false) String username,
                                        @RequestParam(value = "id", required = false) Long id,
                                        @RequestBody Models.AppUser user) {

        try {
            Models.AppUser userToBeUpdate = dataService.getAllUsers(new UserPredicate(id, name, username), PageRequest.of(0, 1)).getContent().get(0);
            user.setId(userToBeUpdate.getId());

            Models.AppUser updatedUser = dataService.saveAUser(user);
            JsonResponse response = JsonSetSuccessResponse.setResponse(updatedUser != null ? ApiCode.SUCCESS.getCode() : ApiCode.FAILED.getCode(), updatedUser != null ? ApiCode.SUCCESS.getDescription() : ApiCode.FAILED.getDescription(), null, updatedUser);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/saveRole")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<?> saveRole(@RequestBody Models.AppRole appRole) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/saveRole").toUriString());
        log.info("uri saverole ::: {}", uri);

        try {
            Models.AppRole savedRole = dataService.saveARole(appRole);
            JsonResponse response = JsonSetSuccessResponse.setResponse(savedRole != null ? ApiCode.SUCCESS.getCode() : ApiCode.FAILED.getCode(), savedRole != null ? ApiCode.SUCCESS.getDescription() : ApiCode.FAILED.getDescription(), null, savedRole);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/role2user")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/addroletouser").toUriString());
        log.info("uri addroletouser ::: {}", uri);

        try {
            dataService.addARoleToAUser(form.getUsername(), form.getRoleName());
            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping(value = {"/token/refresh"})
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        TokenHelper.refreshToken(request, response);
    }

    @DeleteMapping(value = "delete")
    @PreAuthorize("hasAuthority('user:delete')")
    public ResponseEntity<?> deleteStudent(HttpServletRequest request,
                                           @RequestParam(value = "name", required = false) String name,
                                           @RequestParam(value = "username", required = false) String username,
                                           @RequestParam(value = "id", required = false) Long id) {
        try {
            Models.AppUser userToBeDeleted = dataService.getAllUsers(new UserPredicate(id, name, username), PageRequest.of(0, 1)).getContent().get(0);
            userToBeDeleted.setId(userToBeDeleted.getId());
            userToBeDeleted.setDeleted(true);

            Models.AppUser updatedUser = dataService.saveAUser(userToBeDeleted);
            JsonResponse response = JsonSetSuccessResponse.setResponse(updatedUser != null ? ApiCode.SUCCESS.getCode() : ApiCode.FAILED.getCode(), updatedUser != null ? ApiCode.SUCCESS.getDescription() : ApiCode.FAILED.getDescription(), null, updatedUser);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}

