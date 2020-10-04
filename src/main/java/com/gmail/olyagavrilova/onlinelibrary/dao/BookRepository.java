package com.gmail.olyagavrilova.onlinelibrary.dao;

import com.gmail.olyagavrilova.onlinelibrary.dao.entity.Book;
import com.gmail.olyagavrilova.onlinelibrary.dao.entity.Subscription;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.CrudRepository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface BookRepository  extends CrudRepository<Book, Long>  {

    List<Book> findAll();

    List<Book> findBooksByAuthorIsLike(String author);

    List<Book> findBooksByTitleIsContaining(String title);

    List<Book> findBooksByPublisherIsLike(String publisher);

    Optional<Book> findBookByIdAndQuantityIsGreaterThan(long bookId, int quantity);


}