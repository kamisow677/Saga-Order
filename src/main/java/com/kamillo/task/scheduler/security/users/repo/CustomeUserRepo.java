package com.kamillo.task.scheduler.security.users.repo;

import com.kamillo.task.scheduler.security.users.CustomeUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomeUserRepo extends JpaRepository<CustomeUser, Long> {

    Optional<CustomeUser> findByUsername(String username);

//    @Query("SELECT u FROM CustomeUser u " +
//            "JOIN FETCH u.roles " +
//            "WHERE u.username=:username ")
//    Optional<CustomeUser> getUserWihtRoles(@Param("username") String username);


}
