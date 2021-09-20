package com.security.spring.api.repository;

import com.security.spring.api.domain.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepo extends JpaRepository<AppUser, Long>, JpaSpecificationExecutor<AppUser> {
    AppUser findByUsername(String username);
}
