package com.invoicegenerator.invoicegenerator.UserTest;

import com.invoicegenerator.invoicegenerator.model.Users.Role;
import com.invoicegenerator.invoicegenerator.model.Users.User;
import com.invoicegenerator.invoicegenerator.repository.UserRepository;
import com.invoicegenerator.invoicegenerator.services.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserServiceImpl userServiceImpl = new UserServiceImpl();

    @Test
    public void testCreateUser() {
        User user = new User();

        user.setEmail("ravikumar@gmail.com");
        user.setPassword("ravi2020");
        user.setFirstName("Ravi");
        user.setLastName("Kumar");

        User savedUser = userServiceImpl.save(user);

        User existUser = userServiceImpl.findByEmail(user.getEmail());

        assertThat(savedUser.getFirstName()).isEqualTo(existUser.getFirstName());

    }
}
