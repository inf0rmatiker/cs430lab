package com.company;

import java.util.ArrayList;
import java.util.List;

public class Member {
    public Integer id;
    public String firstName;
    public String lastName;
    public String DOB;
    public Character gender;

    public List<Checkout> checkouts;

    public Member(Integer id, String firstName, String lastName, String DOB, Character gender) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.DOB = isoDateFormat(DOB);
        this.gender = gender;
        this.checkouts = new ArrayList<Checkout>();
    }

    public String isoDateFormat(String date) {
        String[] parts = date.split("/");
        return String.format("%s-%s-%s", parts[2], parts[0], parts[1]);
    }

    public void addCheckout(Checkout checkout) {
        checkouts.add(checkout);
    }

    @Override
    public String toString() {
        return String.format("(%d,'%s','%s','%s','%c')", id, firstName, lastName, DOB, gender);
    }
}
