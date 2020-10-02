package com.gmail.olyagavrilova.onlinelibrary.controller;

import com.gmail.olyagavrilova.onlinelibrary.service.BookService;
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
public class AdminController {
private final UserService service;
private final BookService bookService;

    @GetMapping(value = "/users")
    public ModelAndView findAllUsersForAdminPage() {
        ModelAndView modelAndView= new ModelAndView("modifyUser");
        modelAndView.getModelMap().addAttribute("users", service.findAllReaders());
        return modelAndView;
    }

    @GetMapping(value = "/users/block")
    public ModelAndView changeEnableStatusOfUser(@RequestParam String userId) {
        service.changeUsersEnable(Integer.parseInt(userId));

        return findAllUsersForAdminPage();
    }

    @PostMapping("/add/book")
    public ModelAndView  login(@RequestParam String title, @RequestParam String author, @RequestParam String publisher,
                              @RequestParam String quantity, @RequestParam String year) {

        if (StringUtils.isEmpty(title) || StringUtils.isEmpty(author) || StringUtils.isEmpty(publisher)
                || StringUtils.isEmpty(quantity) || StringUtils.isEmpty(year)) {
            ModelAndView modelAndView = new ModelAndView("admin");
            modelAndView.getModelMap().addAttribute("errorMessage", "fill.form");
            return modelAndView;
        }

        bookService.createBook(title,author,publisher,quantity,year);

        ModelAndView modelAndView = new ModelAndView("admin");
        modelAndView.getModelMap().addAttribute("errorMessage", "book.created");

        return modelAndView;
    }
}
