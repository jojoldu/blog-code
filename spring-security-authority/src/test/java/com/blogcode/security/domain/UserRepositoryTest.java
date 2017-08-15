package com.blogcode.security.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by jojoldu@gmail.com on 2017. 8. 15.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Test
    public void User와_UserRole은_ManyToMany_관계로호출된다 () throws Exception {

        UserRole userRole = userRoleRepository.save(new UserRole("ROLE_USER"));
        User user = User.builder()
                .email("jojoldu@gmail.com")
                .imageUrl("https://avatars3.githubusercontent.com/u/7760496?v=4&s=460")
                .name("jojoldu")
                .build();
        user.addRole(userRole);
        userRepository.save(user);

        User savedUser = userRepository.findByEmail("jojoldu@gmail.com");

        assertThat(savedUser.getUserRoles().size(), is(1));
        assertThat(savedUser.getUserRoles().get(0).getUserRole().getRole(), is("ROLE_USER"));
    }
}
