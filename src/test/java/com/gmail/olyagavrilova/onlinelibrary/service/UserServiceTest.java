package com.gmail.olyagavrilova.onlinelibrary.service;

import com.gmail.olyagavrilova.onlinelibrary.dao.UserRepository;
import com.gmail.olyagavrilova.onlinelibrary.dao.entity.Authority;
import com.gmail.olyagavrilova.onlinelibrary.dao.entity.User;
import com.gmail.olyagavrilova.onlinelibrary.dto.UserDto;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void findAllReaders_BookIsMappedToBookDto() {
        List<User> list= new ArrayList<>();
        list.add(generateUserTestData());

        Mockito.doReturn(list).when(userRepository).findAll();

        List<UserDto> result = userService.findAllReaders();

        Assert.assertEquals(list.size(), result.size());
        Assert.assertEquals(list.get(0).getUsername(), result.get(0).getUsername());

    }

    private User generateUserTestData() {
        User user= new User();

        user.setId(1);
        user.setUsername("test");
        user.setPassword("111");
        user.setEnabled(true);

        return user;
    }
}