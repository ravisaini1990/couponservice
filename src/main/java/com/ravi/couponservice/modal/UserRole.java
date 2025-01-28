package com.ravi.couponservice.modal;

import jakarta.persistence.*;

@Entity
@Table(name = "user_role")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int user_id;

    @Column
    private int role_id;

    public UserRole() {
    }

    public UserRole(int userId, int role_id) {
        user_id = userId;
        this.role_id = role_id;
    }


    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
