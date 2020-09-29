package com.gmail.olyagavrilova.onlinelibrary.dao;

import com.gmail.olyagavrilova.onlinelibrary.dao.entity.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Long> {

    List<Book> findAll();

    List<Book> findBooksByAuthorIsLike(String author);

    List<Book> findBooksByTitleIsContaining(String title);

    List <Book> findBooksByPublisherIsLike(String publisher);


}
