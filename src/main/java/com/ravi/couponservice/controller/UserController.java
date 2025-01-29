package com.ravi.couponservice.controller;

import com.ravi.couponservice.modal.Role;
import com.ravi.couponservice.modal.User;
import com.ravi.couponservice.repository.UserRepository;
import com.ravi.couponservice.security.SecurityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashSet;
import java.util.Set;

@Controller
public class UserController {

    @Autowired
    SecurityService securityService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String showLoginPage() {
        return "login"; //name of html in resource folder
    }

    @GetMapping("/showReg")
    public String showRegisterPage() {
        return "/registerUser"; //name of html in resource folder
    }

    @PostMapping("/registerUser")
    public String registerUser(User user) {
        Set<Role> roleSet = new HashSet<>();
        Role userRole = new Role();
        userRole.setId(2); //2 is for type User and 1 for Admin , Can be taken via UI of Register page too
        roleSet.add(userRole);

        user.setRoles(roleSet);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(String email, String password, HttpServletRequest request, HttpServletResponse response) {
        boolean result = securityService.login(email, password, request, response);
        System.out.println("login Response" + result);
        if (result) {
            return "index";
        }
        return "login";

    }
}
