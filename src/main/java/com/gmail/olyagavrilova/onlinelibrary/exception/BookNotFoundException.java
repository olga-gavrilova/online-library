package com.gmail.olyagavrilova.onlinelibrary.exception;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(String message) {
        super(message);
    }
}
