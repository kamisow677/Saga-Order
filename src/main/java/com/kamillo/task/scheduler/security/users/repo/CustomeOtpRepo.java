package com.kamillo.task.scheduler.security.users.repo;

import com.kamillo.task.scheduler.security.users.CustomeUser;
import com.kamillo.task.scheduler.security.users.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomeOtpRepo extends JpaRepository<Otp, Long> {
    List<Otp> findOtpByUsername(String username);
}
