package com.gmail.olyagavrilova.onlinelibrary.controller;

import com.gmail.olyagavrilova.onlinelibrary.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @GetMapping("/registration")
    public ModelAndView getRegisterPage() {
        return new ModelAndView("registrationForm");
    }

    @PostMapping("/registration")
    public ModelAndView login(@RequestParam String username, @RequestParam String password, @RequestParam String role) {

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password) || StringUtils.isEmpty(role)) {
            ModelAndView modelAndView = new ModelAndView("registrationForm");
            modelAndView.getModelMap().addAttribute("errorMessage", "fill.form");
            return modelAndView;
        }

        userService.createUser(username, password, role);

        ModelAndView modelAndView;

        if (role.equalsIgnoreCase("reader")) {
            modelAndView = new ModelAndView("redirect:/books");
        } else {
            modelAndView = new ModelAndView("redirect:/users");
        }

        return modelAndView;
    }
}
