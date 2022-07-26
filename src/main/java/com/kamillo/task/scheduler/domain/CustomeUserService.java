package com.kamillo.task.scheduler.domain;

import com.kamillo.task.scheduler.security.users.CustomeRole;
import com.kamillo.task.scheduler.security.users.CustomeUser;

import java.util.List;
import java.util.Optional;

public interface CustomeUserService {
    CustomeUser saveCustomeUser(CustomeUser customeUser);
    CustomeRole saveCustomeRole(CustomeRole customeRole);
    void addRoleToUser(String username, CustomeRole.ROLE role);
    Optional<CustomeUser> getCustomeUser(String username);
    List<CustomeUser> getCustomeUsers();
}
