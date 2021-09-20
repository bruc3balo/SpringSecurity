package com.security.spring.api.repository;

import com.security.spring.api.domain.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AppRoleRepo extends JpaRepository<AppRole, Long>, JpaSpecificationExecutor<AppRole> {
    AppRole findByName(String roleName);
}
