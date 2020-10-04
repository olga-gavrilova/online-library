package com.gmail.olyagavrilova.onlinelibrary.dao.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@ToString(exclude = {"subscriptions"})
@EqualsAndHashCode(exclude = {"subscriptions"})
@Entity(name="books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private String author;

    private String publisher;

    private int quantity;

    @Column(name = "year_of_publishing" )
    private int yearOfPublishing;

    @OneToMany(mappedBy = "book")
    private Set<Subscription> subscriptions = new HashSet<>();

}
