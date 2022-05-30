package com.invoicegenerator.invoicegenerator.repository;


import com.invoicegenerator.invoicegenerator.model.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);
}
