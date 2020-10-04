package com.gmail.olyagavrilova.onlinelibrary.controller;

import com.gmail.olyagavrilova.onlinelibrary.dao.entity.Book;
import com.gmail.olyagavrilova.onlinelibrary.exception.BookNotFoundException;
import com.gmail.olyagavrilova.onlinelibrary.exception.SearchOptionNotSupported;
import com.gmail.olyagavrilova.onlinelibrary.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
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
        return findAllBooksWithPagination(Optional.of(1), Optional.of(5));
    }

    @RequestMapping(value = "/books", method = RequestMethod.GET)
    public ModelAndView findAllBooksWithPagination(@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        Page<Book> bookPage = bookService.findAllBooksWithPagination(PageRequest.of(currentPage - 1, pageSize));

        ModelAndView model = new ModelAndView("index");
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

    @GetMapping("/reader/books")
    public ModelAndView redirectToAllBooks() {
        ModelAndView modelAndView = new ModelAndView("readerBooksView");
        modelAndView.getModelMap().addAttribute("books", bookService.findAllBooks());

        return modelAndView;
    }

    @GetMapping(value = "/reader/books/search")
    public ModelAndView findBooksBySearchOption(@RequestParam String searchOption, @RequestParam String searchValue) {
        ModelAndView modelAndView = new ModelAndView("readerBooksView");

        if (StringUtils.isEmpty(searchValue)) {
            throw new SearchOptionNotSupported();
        }

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
                throw new SearchOptionNotSupported();
        }

        return modelAndView;
    }

    @GetMapping(value = "/reader/books/add")
    public ModelAndView addBookToSubscription(@RequestParam String bookId) {
        bookService.addBookToSubscriptionForUser(Integer.parseInt(bookId));

        ModelAndView modelAndView = new ModelAndView("readerBooksView");
        modelAndView.getModelMap().addAttribute("books", bookService.findAllBooks());
        modelAndView.getModelMap().addAttribute("message", "");

        return modelAndView;
    }

    @GetMapping(value = "/reader/books/return")
    public ModelAndView removeBookFromSubscription(@RequestParam String bookId) {
        bookService.removeBookFromSubscriptionForUser(Long.parseLong(bookId));

        ModelAndView modelAndView = new ModelAndView("readerSubscriptionsView");
        modelAndView.getModelMap().addAttribute("subscriptions", bookService.findSubscriptions());
        modelAndView.getModelMap().addAttribute("message", "");

        return modelAndView;
    }

    @GetMapping("/reader/subscriptions")
    public ModelAndView redirectToSubscriptionsForUser() {
        ModelAndView modelAndView = new ModelAndView("readerSubscriptionsView");
        modelAndView.getModelMap().addAttribute("subscriptions", bookService.findSubscriptions());

        return modelAndView;
    }

    @ExceptionHandler(SearchOptionNotSupported.class)
    public ModelAndView handleSearchError() {
        ModelAndView modelAndView = new ModelAndView("readerBooksView");
        modelAndView.addObject("errorMessageSearch", "");
        modelAndView.getModelMap().addAttribute("books", bookService.findAllBooks());

        return modelAndView;
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ModelAndView handleAddBookError() {
        ModelAndView modelAndView = new ModelAndView("readerBooksView");
        modelAndView.addObject("errorMessageBook", "");
        modelAndView.getModelMap().addAttribute("books", bookService.findAllBooks());

        return modelAndView;
    }
}
