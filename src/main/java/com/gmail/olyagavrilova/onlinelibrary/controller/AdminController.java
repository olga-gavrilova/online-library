package com.gmail.olyagavrilova.onlinelibrary.controller;

import com.gmail.olyagavrilova.onlinelibrary.service.BookService;
import com.gmail.olyagavrilova.onlinelibrary.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AdminController {
private final UserService service;
private final BookService bookService;

    @GetMapping("/admin/books")
    public ModelAndView redirectToCreateBook() {
        return new ModelAndView("adminBooksView");
    }

    @PostMapping("/admin/books/add")
    public ModelAndView  login(@RequestParam String title, @RequestParam String author, @RequestParam String publisher,
                              @RequestParam String quantity, @RequestParam String year) {

        if (StringUtils.isEmpty(title) || StringUtils.isEmpty(author) || StringUtils.isEmpty(publisher)
                || StringUtils.isEmpty(quantity) || StringUtils.isEmpty(year)) {
            ModelAndView modelAndView = new ModelAndView("adminBooksView");
            modelAndView.getModelMap().addAttribute("errorMessage", "");
            return modelAndView;
        }

        bookService.createBook(title,author,publisher,quantity,year);

        ModelAndView modelAndView = new ModelAndView("adminBooksView");
        modelAndView.getModelMap().addAttribute("message", "");

        log.info("AdminController - login() method worked");

        return modelAndView;
    }

    @GetMapping("/admin/users")
    public ModelAndView redirectToManageUsers() {
        ModelAndView modelAndView= new ModelAndView("adminUsersView");
        modelAndView.getModelMap().addAttribute("users", service.findAllReaders());

        log.info("AdminController -  findAllUsersForAdminPage() method ");

        return modelAndView;
    }

    @GetMapping(value = "/admin/users/block")
    public ModelAndView changeEnableStatusOfUser(@RequestParam String userId) {
        service.changeUsersEnable(Integer.parseInt(userId));

        log.info("AdminController - changeEnableStatusOfUser() method worked and change enable status of User with id = {}", userId);

        ModelAndView modelAndView= new ModelAndView("adminUsersView");
        modelAndView.getModelMap().addAttribute("users", service.findAllReaders());

        return modelAndView;
    }
}
