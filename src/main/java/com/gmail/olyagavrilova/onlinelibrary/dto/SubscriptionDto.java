package com.gmail.olyagavrilova.onlinelibrary.dto;

import lombok.Data;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class SubscriptionDto {

    private long bookId;

    @NotBlank
    private String  bookTitle;

    @NotBlank
    private String  author;

    @NotBlank
    private String  publisher;

    @NotNull
    private LocalDate dateOfReturn;


}
