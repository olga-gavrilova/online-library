package com.gmail.olyagavrilova.onlinelibrary.dao;

import com.gmail.olyagavrilova.onlinelibrary.dao.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);
}