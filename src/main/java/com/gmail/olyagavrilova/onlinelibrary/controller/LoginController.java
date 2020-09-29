package com.gmail.olyagavrilova.onlinelibrary.controller;

import com.gmail.olyagavrilova.onlinelibrary.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

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
    public ModelAndView login(@RequestParam String username, @RequestParam String password, @RequestParam String role) {

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password) || StringUtils.isEmpty(role)) {
            ModelAndView modelAndView = new ModelAndView("registrationForm");
            modelAndView.getModelMap().addAttribute("errorMessage", "Please, fill the registration form!");
            return modelAndView;
        }

        userService.createUser(username, password, role);

        ModelAndView modelAndView = new ModelAndView("bookManager");
        modelAndView.getModelMap().addAttribute("welcomeMessage", "Welcome to Online Library, " + username + "!");
        return modelAndView;
    }
}
