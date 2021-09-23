package com.security.spring.config.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.security.spring.config.security.AppUserPermission.*;


public enum AppRolesEnum {

    ROLE_ADMIN(Sets.newHashSet(USER_READ, USER_WRITE, USER_DELETE, USER_UPDATE, PRODUCT_READ, PRODUCT_WRITE, PRODUCT_DELETE, PRODUCT_UPDATE, PRODUCT_CATEGORY_READ, PRODUCT_CATEGORY_WRITE, PRODUCT_CATEGORY_DELETE, PRODUCT_CATEGORY_UPDATE)),
    ROLE_ADMIN_TRAINEE(Sets.newHashSet(USER_READ, PRODUCT_READ, PRODUCT_CATEGORY_READ)),
    ROLE_USER(Sets.newHashSet(USER_UPDATE, USER_READ));


    private final Set<AppUserPermission> permissions;

    AppRolesEnum(Set<AppUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<AppUserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream().map(permission -> new SimpleGrantedAuthority(permission.getPermission())).collect(Collectors.toSet());

        permissions.add(new SimpleGrantedAuthority(this.name()));
        return permissions;
    }
}
