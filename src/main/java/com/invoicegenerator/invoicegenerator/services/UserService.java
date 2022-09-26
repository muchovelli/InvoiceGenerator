package com.invoicegenerator.invoicegenerator.services;

import com.invoicegenerator.invoicegenerator.model.Users.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    User save(User user);
    User findByEmail(String email);
    List<User> findAll();

}
