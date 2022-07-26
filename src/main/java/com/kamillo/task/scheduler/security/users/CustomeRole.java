package com.kamillo.task.scheduler.security.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.ManyToMany;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;

import java.util.Set;

import static javax.persistence.GenerationType.AUTO;

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
