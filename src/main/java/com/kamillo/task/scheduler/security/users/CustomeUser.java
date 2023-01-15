package com.kamillo.task.scheduler.security.users;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.GenerationType.AUTO;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
public class CustomeUser {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    private String username;
    private String password;
    private String nick;

    @Email
    private String email;

    @ManyToMany(mappedBy = "users")
    private Set<CustomeRole> roles = new HashSet<>();

    public void addRole(CustomeRole customeRole) {
        this.roles.add(customeRole);
        customeRole.getUsers().add(this);
    }

    public void deleteRole(CustomeRole customeRole) {
        roles.remove(customeRole);
        customeRole.getUsers().remove(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<CustomeRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<CustomeRole> roles) {
        this.roles = roles;
    }
}
