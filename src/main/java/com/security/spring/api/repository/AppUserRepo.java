package com.security.spring.api.repository;

import com.security.spring.api.domain.Models;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepo extends JpaRepository<Models.AppUser, Long>, JpaSpecificationExecutor<Models.AppUser> {
    Models.AppUser findByUsername(String username);
}
