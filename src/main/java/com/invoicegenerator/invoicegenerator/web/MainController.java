package com.invoicegenerator.invoicegenerator.web;

import com.invoicegenerator.invoicegenerator.model.Users.User;
import com.invoicegenerator.invoicegenerator.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String home() {
        return "index";
    }
}