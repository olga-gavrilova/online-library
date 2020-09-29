package com.gmail.olyagavrilova.onlinelibrary.model;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class BookDto {

    private int id;

    @NotBlank
    private String  title;

    @NotBlank
    private String  author;

    @NotBlank
    private String  publisher;

    @NotNull
    @Min(value = 1)
    private int quantity;

    private int yearOfPublishing;
}
