package com.gmail.olyagavrilova.onlinelibrary.service;

import com.gmail.olyagavrilova.onlinelibrary.dao.BookRepository;
import com.gmail.olyagavrilova.onlinelibrary.dao.SubscriptionRepository;
import com.gmail.olyagavrilova.onlinelibrary.dao.UserRepository;
import com.gmail.olyagavrilova.onlinelibrary.dao.entity.Authority;
import com.gmail.olyagavrilova.onlinelibrary.dao.entity.Book;
import com.gmail.olyagavrilova.onlinelibrary.dao.entity.Subscription;
import com.gmail.olyagavrilova.onlinelibrary.dao.entity.User;
import com.gmail.olyagavrilova.onlinelibrary.model.BookDto;
import com.gmail.olyagavrilova.onlinelibrary.model.SubscriptionDto;
import com.gmail.olyagavrilova.onlinelibrary.exception.BookNotFoundException;
import com.gmail.olyagavrilova.onlinelibrary.service.mapper.BookMapper;
import com.gmail.olyagavrilova.onlinelibrary.service.mapper.SubscriptionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final SubscriptionMapper subscriptionMapper;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;


    public void createBook(BookDto dto) {
        bookRepository.save(bookMapper.map(dto, Book.class));
    }

    public List<BookDto> findAllBooks() {
        return bookRepository.findAll().stream()
                .map(book -> bookMapper.map(book, BookDto.class))
                .collect(Collectors.toList());
    }

    public List<BookDto> findBooksByTitle(String title) {
        return bookRepository.findBooksByTitleIsContaining(title).stream()
                .map(book -> bookMapper.map(book, BookDto.class))
                .collect(Collectors.toList());
    }

    public List<BookDto> findBooksByAuthor(String author) {
        return bookRepository.findBooksByAuthorIsLike(author).stream()
                .map(book -> bookMapper.map(book, BookDto.class))
                .collect(Collectors.toList());
    }

    public List<BookDto> findBooksByPublisher(String publisher) {
        return bookRepository.findBooksByPublisherIsLike(publisher).stream()
                .map(book -> bookMapper.map(book, BookDto.class))
                .collect(Collectors.toList());
    }

    public List<SubscriptionDto> findSubscriptions() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User with such name was not found"));

        List<SubscriptionDto> subscriptionDtos = new ArrayList<>();

        for (Subscription subscription : user.getSubscriptions()) {
            subscriptionDtos.add(subscriptionMapper.map(subscription, SubscriptionDto.class));
        }

        return subscriptionDtos;
    }

    @Transactional
    public void addBookToSubscriptionForUser(int bookId) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User with such name was not found"));

        Book book = bookRepository.findBookByIdAndQuantityIsGreaterThan(bookId, 0)
                .orElseThrow(() -> new BookNotFoundException("Book with such id was not found"));

        Subscription subscription = new Subscription();
        subscription.setBook(book);
        subscription.setUser(user);
        subscription.setDateOfReturn(LocalDate.now().plusMonths(2));

        subscriptionRepository.save(subscription);

        book.setQuantity(book.getQuantity() - 1);
        bookRepository.save(book);
    }

    @Transactional
    public void removeBookFromSubscriptionForUser(long bookId) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        Book book = bookRepository.findBookByIdAndQuantityIsGreaterThan(bookId, 0)
                .orElseThrow(() -> new BookNotFoundException("Book with such id was not found"));

        for (Subscription subscription : book.getSubscriptions()) {
            if (subscription.getBook().getId() == bookId && subscription.getUser().getUsername().equals(userName)) {
                subscriptionRepository.deleteById(subscription.getId());
                break;
            }
        }

        book.setQuantity(book.getQuantity() + 1);

        bookRepository.save(book);
    }


    public void createBook(String bookTitle, String bookAuthor, String bookPublisher,
                           String bookQuantity, String bookYearOfPublishing) {
        Book book= new Book();
        book.setTitle(bookTitle);
        book.setAuthor(bookAuthor);
        book.setPublisher(bookPublisher);
        book.setQuantity(Integer.parseInt(bookQuantity));
        book.setYearOfPublishing(Integer.parseInt(bookYearOfPublishing));

        bookRepository.save(book);
    }


    public Page<Book> findPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Book> list = bookRepository.findAll();

        if (list.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, list.size());
            list = list.subList(startItem, toIndex);
        }

        Page<Book> bookPage = new PageImpl<Book>(list, PageRequest.of(currentPage, pageSize), bookRepository.findAll().size());

        return bookPage;
    }

}
