package com.company;

public class Phone {

    public String number;
    public String type;

    public Phone(String number, String type) {
        this.number = number;
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("('%s','%s')", number, type);
    }
}
