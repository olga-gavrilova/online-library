package com.gmail.olyagavrilova.onlinelibrary.service;

import com.gmail.olyagavrilova.onlinelibrary.dao.BookRepository;
import com.gmail.olyagavrilova.onlinelibrary.dao.entity.Book;
import com.gmail.olyagavrilova.onlinelibrary.model.BookDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper mapper;

    public void createBook(BookDto dto) {
        bookRepository.save(mapper.map(dto, Book.class));
    }

    public List<BookDto> findAllBooks(){
        return bookRepository.findAll().stream()
                .map(book -> mapper.map(book, BookDto.class))
                .collect(Collectors.toList());
    }

    public List<BookDto> findBooksByTitle(String title){
        return bookRepository.findBooksByTitleIsContaining(title).stream()
                .map(book -> mapper.map(book, BookDto.class))
                .collect(Collectors.toList());
    }

    public List<BookDto> findBooksByAuthor(String author){
        return bookRepository.findBooksByAuthorIsLike(author).stream()
                .map(book -> mapper.map(book, BookDto.class))
                .collect(Collectors.toList());
    }

    public List<BookDto> findBooksByPublisher(String publisher){
        return bookRepository.findBooksByPublisherIsLike(publisher).stream()
                .map(book -> mapper.map(book, BookDto.class))
                .collect(Collectors.toList());
    }

}
