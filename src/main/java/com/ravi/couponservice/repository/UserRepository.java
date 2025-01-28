package com.ravi.couponservice.repository;

import com.ravi.couponservice.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

   User findByEmail(String email);
}
