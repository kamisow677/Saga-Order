package com.kamillo.task.scheduler.security.service;

import com.kamillo.task.scheduler.security.users.CustomeRole;
import com.kamillo.task.scheduler.security.users.CustomeUser;
import com.kamillo.task.scheduler.domain.CustomeUserService;
import com.kamillo.task.scheduler.security.users.repo.CustomeRoleRepo;
import com.kamillo.task.scheduler.security.users.repo.CustomeUserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomeUserServiceImpl implements CustomeUserService, UserDetailsService {

    private final CustomeUserRepo customeUserRepo;
    private final CustomeRoleRepo customeRoleRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public CustomeUser saveCustomeUser(CustomeUser customeUser) {
        log.info("Saving user with username: " + customeUser.getUsername() + " and roles: " + customeUser.getRoles());
        customeUser.setPassword(passwordEncoder.encode(customeUser.getPassword()));
        return customeUserRepo.save(customeUser);
    }

    @Override
    public CustomeRole saveCustomeRole(CustomeRole customeRole) {
        log.info("Saving role with name: " + customeRole.getName());
        return customeRoleRepo.save(customeRole);
    }

    @Override
    @Transactional
    public void addRoleToUser(String username, CustomeRole.ROLE roleName) {
        log.info("Adding role " + roleName +" with name: " + username);
        CustomeUser customeUser = customeUserRepo.findByUsername(username)
                .orElseThrow(() ->  new NoSuchUserException(username));
        CustomeRole customeRole = customeRoleRepo.findByName(roleName)
                .orElseThrow(() ->  new NoSuchRoleException(roleName));
        customeUser.addRole(customeRole);
        customeUserRepo.save(customeUser);
    }

    @Override
    public Optional<CustomeUser> getCustomeUser(String username) {
        log.info("Get user: " + username);
        return customeUserRepo.findByUsername(username);
    }

    @Override
    public List<CustomeUser> getCustomeUsers() {
        return customeUserRepo.findAll(Sort.by(Sort.Direction.ASC, "username"));
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomeUser customeUser = customeUserRepo.findByUsername(username)
                .orElseGet(() -> {
                   log.info("Error, no user for username: " + username);
                   throw new UsernameNotFoundException(username);
                });
        log.info("User username: " + username + " found");
        List<SimpleGrantedAuthority> simpleGrantedAuthorityStream = customeUser.getRoles().stream().map(it -> new SimpleGrantedAuthority(it.getName().toString()))
                .collect(Collectors.toList());
        return new User(
                customeUser.getUsername(), customeUser.getPassword(), simpleGrantedAuthorityStream);
    }

    private class NoSuchUserException extends RuntimeException {
        public NoSuchUserException(String username) {
            super(username);
        }
    }

    private class NoSuchRoleException extends RuntimeException {
        public NoSuchRoleException(CustomeRole.ROLE role) {
            super(role.toString());
        }
    }
}
