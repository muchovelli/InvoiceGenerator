package com.invoicegenerator.invoicegenerator.web;

import com.invoicegenerator.invoicegenerator.model.Users.User;
import com.invoicegenerator.invoicegenerator.repository.UserRepository;
import com.invoicegenerator.invoicegenerator.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserRegistrationController {

    UserRepository userRepository;
    @Autowired
    UserServiceImpl userServiceImpl;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        model.addAttribute("user", new User());
        return "signup_form";
    }

    @PostMapping("/process_register")
    public String processRegister(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userServiceImpl.save(user);
        return "register_success";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> listUsers = userServiceImpl.findAll();
        model.addAttribute("listUsers", listUsers);

        return "users";
    }
}
