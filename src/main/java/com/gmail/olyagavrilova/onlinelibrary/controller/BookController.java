package com.gmail.olyagavrilova.onlinelibrary.controller;

import com.gmail.olyagavrilova.onlinelibrary.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private String parameterOfSearch;

    @GetMapping
    public ModelAndView findAllBookForIndexPage(){
        return findAllBooks("index");
    }

    @GetMapping(value = "/books")
    public ModelAndView findAllBooksForBookManager(){
        return findAllBooks("bookManager");
    }

    private ModelAndView findAllBooks(String viewName) {
        ModelAndView modelAndView =new ModelAndView(viewName);
        modelAndView.getModelMap().addAttribute("books", bookService.findAllBooks());
        return modelAndView;
    }

//    @PostMapping(value = "/books/searchBy")
//    public String readSearchParam(@RequestParam String parameter){
//       parameterOfSearch= parameter;
//        return parameterOfSearch;
//
//    }

    @PostMapping(value = "/books/search")
    public ModelAndView findBooksBySearchOption(@RequestParam String searchOption, @RequestParam String searchValue){

        ModelAndView modelAndView =new ModelAndView("bookManager");

        switch (searchOption){
            case "title":
                modelAndView.getModelMap().addAttribute("books", bookService.findBooksByTitle(searchValue));
                break;

            case "author":

                modelAndView.getModelMap().addAttribute("books", bookService.findBooksByAuthor(searchValue));
                break;

            case "publisher":

                modelAndView.getModelMap().addAttribute("books", bookService.findBooksByPublisher(searchValue));
                break;

        }
        return modelAndView;
    }



}
