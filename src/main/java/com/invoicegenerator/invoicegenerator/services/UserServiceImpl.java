package com.invoicegenerator.invoicegenerator.services;

import com.invoicegenerator.invoicegenerator.model.Users.Role;
import com.invoicegenerator.invoicegenerator.model.Users.User;
import com.invoicegenerator.invoicegenerator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Lazy
    private BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }

    public UserServiceImpl() {

    }

    @Override
    public User save(User user) {
        User user1 = new User(1L, user.getFirstName(),
                user.getLastName(), user.getEmail(),
                user.getPassword(),
                user.getRoles());


        return userRepository.save(user1);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username);
        if(user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    public User findByEmail(String email){
        for(User user : userRepository.findAll()){
            if(user.getEmail().equals(email)){
                return user;
            }
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }


}