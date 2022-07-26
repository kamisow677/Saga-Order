package com.kamillo.task.scheduler.security.users.repo;

import com.kamillo.task.scheduler.security.users.CustomeRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomeRoleRepo extends JpaRepository<CustomeRole, Long> {
    Optional<CustomeRole> findByName(CustomeRole.ROLE name);
}
