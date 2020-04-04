package com.company;

import java.util.ArrayList;
import java.util.List;

public class Book {

    public String isbn;
    public String title;
    public Integer year;
    public Integer pub_id;

    List<Integer> authorIds;

    public Book(String isbn, String title, Integer year, Integer pub_id) {
        this.isbn = isbn;
        this.title = title;
        this.year = year;
        this.pub_id = pub_id;
        this.authorIds = new ArrayList<Integer>();
    }

    public void addAuthorId(Integer authorId) {
        authorIds.add(authorId);
    }

    @Override
    public String toString() {
        return String.format("('%s','%s',%d,%d)", isbn, title, year, pub_id);
    }
}
