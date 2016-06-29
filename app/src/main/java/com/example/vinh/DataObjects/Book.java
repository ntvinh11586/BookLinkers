package com.example.vinh.DataObjects;

/**
 * Created by vinh on 31/05/16.
 */
public class Book {
    public String name;
    public String author;
    public String description;
    public boolean own;
    public String avatar;

    public Book(String name, String author, String description, String avatar, boolean owningBook) {
        this.name = name;
        this.author = author;
        this.description = description;
        this.avatar = avatar;
        this.own = owningBook;
    }
}
