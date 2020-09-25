package com.gmail.olyagavrilova.onlinelibrary.controller;

import com.gmail.olyagavrilova.onlinelibrary.service.UserService;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/login")
    public ModelAndView login(@RequestParam String login, @RequestParam String password) {

        if(login ==null || login.equals("")) {
            ModelAndView modelAndView = new ModelAndView("loginForm");
            modelAndView.getModelMap().addAttribute("errorMessage", "Please, fill the login form!");
            return modelAndView;
        }
        if(userService.findByLoginAndPassword(login,password)!= null){
            return new ModelAndView("bookManager");
        }
        else {
            ModelAndView modelAndView = new ModelAndView("loginForm");
            modelAndView.getModelMap().addAttribute("errorMessage", "User with such login not found!");
            return modelAndView;
        }

    }

    @GetMapping("/registration")
    public ModelAndView getRegisterPage() {
        return new ModelAndView("registrationForm");
    }

    @PostMapping("/registration")
    public ModelAndView login(@RequestParam String login, @RequestParam String password, @RequestParam String role) {
       userService.create(login,password, role);
       ModelAndView modelAndView = new ModelAndView("bookManager");
       modelAndView.getModelMap().addAttribute("welcomeMessage", "Welcome to Online Library, "+ login+"!");
       return modelAndView;
    }
}
