package com.gmail.olyagavrilova.onlinelibrary.service;

import com.gmail.olyagavrilova.onlinelibrary.dao.UserRepository;
import com.gmail.olyagavrilova.onlinelibrary.dao.entity.Authority;
import com.gmail.olyagavrilova.onlinelibrary.dao.entity.User;
import com.gmail.olyagavrilova.onlinelibrary.model.BookDto;
import com.gmail.olyagavrilova.onlinelibrary.model.UserDto;
import com.gmail.olyagavrilova.onlinelibrary.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Max;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("User with username "+ username+ " not found"));
    }

    public void createUser(String username, String password, String role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        Authority authority = new Authority();
        authority.setAuthority("ROLE_" + role);
        authority.setUser(user);

        user.setAuthority(authority);

        userRepository.save(user);
    }


    public List<UserDto> findAllReaders(){

        return userRepository.findAll().stream()
                .map(user -> userMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    //get User byId
    //change enable
    //save User

    public void changeUsersEnable(int id){
        User user= userRepository.findById((long)id)
                .orElseThrow(() -> new UsernameNotFoundException("User with such id was not found"));

        if(user.isEnabled()){
            user.setEnabled(false);
        }else user.setEnabled(true);


        userRepository.save(user);

    }




}
