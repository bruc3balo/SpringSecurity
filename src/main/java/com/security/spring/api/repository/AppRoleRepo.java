package com.security.spring.api.repository;

import com.security.spring.api.domain.Models;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppRoleRepo extends JpaRepository<Models.AppRole, Long>, JpaSpecificationExecutor<Models.AppRole> {
    Optional<Models.AppRole> findByName(String roleName);
}
