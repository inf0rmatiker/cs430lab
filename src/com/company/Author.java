package com.company;

import java.util.ArrayList;
import java.util.List;

public class Author {
    public Integer id;
    public String firstName;
    public String lastName;

    public List<Phone> phones;

    public Author(Integer id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phones = new ArrayList<>();
    }

    public void addPhone(Phone phone) {
        phones.add(phone);
    }

    @Override
    public String toString() {
        // Write (id,firstName,lastName)
        return String.format("(%d,'%s','%s')", id, firstName, lastName);
    }
}
