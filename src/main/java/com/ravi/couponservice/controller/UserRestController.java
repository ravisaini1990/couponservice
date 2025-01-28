package com.ravi.couponservice.controller;

import com.ravi.couponservice.modal.User;
import com.ravi.couponservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user_api")
public class UserRestController {

    @Autowired
    UserRepository userRepository;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
