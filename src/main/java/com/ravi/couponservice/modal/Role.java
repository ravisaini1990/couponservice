package com.ravi.couponservice.modal;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.util.Set;

@Entity
@Table(name = "role")
public class Role implements GrantedAuthority
{

    @Serial
    private static final long serialVersionUID= 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public Set<User> getUserSet() {
        return userSet;
    }

    public void setUserSet(Set<User> userSet) {
        this.userSet = userSet;
    }

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "roles")
    Set<User> userSet;

    @Column
    private String name;

    public Role() {}

    public Role(Set<User> userSet, String name) {
        this.userSet = userSet;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public String getAuthority() {
        return name;
    }
}
