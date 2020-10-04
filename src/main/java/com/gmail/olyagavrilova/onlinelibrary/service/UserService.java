package com.gmail.olyagavrilova.onlinelibrary.service;

import com.gmail.olyagavrilova.onlinelibrary.dao.UserRepository;
import com.gmail.olyagavrilova.onlinelibrary.dao.entity.Authority;
import com.gmail.olyagavrilova.onlinelibrary.dao.entity.User;
import com.gmail.olyagavrilova.onlinelibrary.dto.UserDto;
import com.gmail.olyagavrilova.onlinelibrary.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));
    }

    public void createUser(String username, String password, String role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        Authority authority = new Authority();
        authority.setAuthority("ROLE_" + role);
        authority.setUser(user);
        user.setAuthority(authority);

        log.info("New user with name:  {} and role: {} was registered", username, role);

        userRepository.save(user);
    }


    public List<UserDto> findAllReaders() {
        return userRepository.findAll().stream()
                .map(user -> userMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }


    public void changeUsersEnable(int id) {
        User user = userRepository.findById((long) id)
                .orElseThrow(() -> new UsernameNotFoundException("User with such id was not found"));

        if (user.isEnabled()) {
            user.setEnabled(false);
            log.info("User with id = {} was blocked", id);
        } else {
            user.setEnabled(true);
        }

        log.info("User with id = {} was unblocked", id);

        userRepository.save(user);
    }

}
