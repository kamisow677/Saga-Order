package com.kamillo.task.scheduler.security.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;

import java.util.Set;

import static jakarta.persistence.GenerationType.AUTO;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "role")
public class CustomeRole {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ROLE name;

    @ManyToMany
    @JoinTable(name = "role_user",
            joinColumns = { @JoinColumn(name = "role_id", referencedColumnName = "id") },
            inverseJoinColumns = { @JoinColumn(name = "fk_user", referencedColumnName = "id") })
    private Set<CustomeUser> users;

    public enum ROLE {
        ROLE_USER, ROLE_MANAGER, ROLE_ADMIN, ROLE_SUPER_ADMIN
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ROLE getName() {
        return name;
    }

    public void setName(ROLE name) {
        this.name = name;
    }

    public Set<CustomeUser> getUsers() {
        return users;
    }

    public void setUsers(Set<CustomeUser> users) {
        this.users = users;
    }
}
