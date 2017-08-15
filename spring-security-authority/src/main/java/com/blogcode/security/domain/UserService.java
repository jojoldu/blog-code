package com.blogcode.security.domain;

import com.blogcode.security.GoogleUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jojoldu@gmail.com on 2017. 8. 16.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User getOrSave(GoogleUser google){
        User savedUser = userRepository.findByEmail(google.getEmail());

        if(savedUser == null){
            User newUser = google.toEntity();
            newUser.addRole(new UserRole("ROLE_USER"));
            savedUser = userRepository.save(newUser);
        }

        return savedUser;
    }
}
