package com.gmail.olyagavrilova.onlinelibrary.service;

import com.gmail.olyagavrilova.onlinelibrary.dao.UserRepository;
import com.gmail.olyagavrilova.onlinelibrary.model.Role;
import com.gmail.olyagavrilova.onlinelibrary.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findByLoginAndPassword(String login, String password) {
        return userRepository.findByLoginAndPassword(login, password);
    }

    public void create(String login, String password, String role){
         userRepository.save(new User(login,password,Role.valueOf(role)));
    }
}
