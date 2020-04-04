package com.company;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Library {

    public String name;
    public String street;
    public String city;
    public String state;

    List<Shelf> shelves;

    public Library(String name, String street, String city, String state) {
        this.name = name;
        this.street = street;
        this.city = city;
        this.state = state;
        this.shelves = new ArrayList<Shelf>();
    }

    @Override
    public String toString() {
        return String.format("('%s','%s','%s','%s')", name, street, city, state);
    }
}
