package com.gmail.olyagavrilova.onlinelibrary.controller;

import com.gmail.olyagavrilova.onlinelibrary.dao.entity.Book;
import com.gmail.olyagavrilova.onlinelibrary.exception.SearchOptionNotSupported;
import com.gmail.olyagavrilova.onlinelibrary.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ModelAndView findAllBooksForIndexPage() {
        return findAllBooks("index");
    }

    @GetMapping("/loggedIn")
    public ModelAndView getSuccessfulLoginPage() {
        GrantedAuthority authority = SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next();

        if (authority.getAuthority().equals("ROLE_READER")) {
            return findAllBooksAndSubscriptionsForBooksPage();
        } else {
            return new ModelAndView("admin");
        }
    }

    @GetMapping("/bookManager")
    public ModelAndView getBookManager() {
        return new ModelAndView("bookManager");
    }

    @GetMapping(value = "/books")
    public ModelAndView findAllBooksAndSubscriptionsForBooksPage() {
        return findAllBooksAndSubscriptions();
    }


//    @GetMapping(value = "/catalog")
//    public ModelAndView findAllBooksForAdminPage() {
//        return findAllBooks("createBook");
//    }





    private ModelAndView findAllBooks(String viewName) {
        ModelAndView modelAndView = new ModelAndView(viewName);
        modelAndView.getModelMap().addAttribute("books", bookService.findAllBooks());

        return modelAndView;
    }

    private ModelAndView findAllBooksAndSubscriptions() {
        ModelAndView modelAndView = findAllBooks("bookManager");
        modelAndView.getModelMap().addAttribute("subscriptions", bookService.findSubscriptions());

        return modelAndView;
    }

    @PostMapping(value = "/books/search")
    public ModelAndView findBooksBySearchOption(@RequestParam String searchOption, @RequestParam String searchValue) {
        ModelAndView modelAndView = new ModelAndView("bookManager");

        switch (searchOption) {
            case "title":
                modelAndView.getModelMap().addAttribute("books", bookService.findBooksByTitle(searchValue));
                modelAndView.getModelMap().addAttribute("subscriptions", bookService.findSubscriptions());
                break;
            case "author":
                modelAndView.getModelMap().addAttribute("books", bookService.findBooksByAuthor(searchValue));
                modelAndView.getModelMap().addAttribute("subscriptions", bookService.findSubscriptions());
                break;
            case "publisher":
                modelAndView.getModelMap().addAttribute("books", bookService.findBooksByPublisher(searchValue));
                modelAndView.getModelMap().addAttribute("subscriptions", bookService.findSubscriptions());
                break;
            default:
                throw new SearchOptionNotSupported("The search option is not supported");
        }

        return modelAndView;
    }

    @GetMapping(value = "/books/add")
    public ModelAndView addBookToSubscription(@RequestParam String bookId) {
        bookService.addBookToSubscriptionForUser(Integer.parseInt(bookId));
        return findAllBooksAndSubscriptions();
    }

    @GetMapping(value = "/books/return")
    public ModelAndView removeBookFromSubscription(@RequestParam String bookId) {
        bookService.removeBookFromSubscriptionForUser(Long.parseLong(bookId));
        return findAllBooksAndSubscriptions();
    }


//

    @RequestMapping(value = "/catalog", method = RequestMethod.GET)
    public ModelAndView listBooks(
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        Page<Book> bookPage = bookService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
ModelAndView model = new ModelAndView("createBook");
        model.getModelMap().addAttribute("bookPage", bookPage);

        int totalPages = bookPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.getModelMap().addAttribute("pageNumbers", pageNumbers);
        }

        return model;
    }



}
