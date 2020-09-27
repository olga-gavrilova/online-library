package com.gmail.olyagavrilova.onlinelibrary.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final JdbcUserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public ModelAndView getLoginPage() {
        return new ModelAndView("loginForm");
    }

    @GetMapping("/bookManager")
    public ModelAndView getBookManager() {
        return new ModelAndView("bookManager");
    }

    @GetMapping("/registration")
    public ModelAndView getRegisterPage() {
        return new ModelAndView("registrationForm");
    }

    @PostMapping("/registration")
    public ModelAndView login(@RequestParam String login, @RequestParam String password, @RequestParam String role) {

        if (StringUtils.isEmpty(login) || StringUtils.isEmpty(password) || StringUtils.isEmpty(role)) {
            ModelAndView modelAndView = new ModelAndView("registrationForm");
            modelAndView.getModelMap().addAttribute("errorMessage", "Please, fill the registration form!");
            return modelAndView;
        }

        userDetailsManager.createUser(User.builder()
                .username(login)
                .password(passwordEncoder.encode(password))
                .roles(role.toUpperCase())
                .build());

        ModelAndView modelAndView = new ModelAndView("bookManager");
        modelAndView.getModelMap().addAttribute("welcomeMessage", "Welcome to Online Library, " + login + "!");
        return modelAndView;
    }
}
