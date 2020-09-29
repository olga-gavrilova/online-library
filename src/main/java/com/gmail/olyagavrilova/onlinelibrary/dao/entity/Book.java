package com.gmail.olyagavrilova.onlinelibrary.dao.entity;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity(name="books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String  title;

    private String  author;

    private String  publisher;

    @Column(name = "year_of_publishing" )
    private int yearOfPublishing;
}
