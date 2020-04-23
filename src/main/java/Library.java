import java.util.HashMap;

import java.util.Map;


public class Library {

    public String name;
    public String street;
    public String city;
    public String state;

    Map<Integer, Shelf> shelves;

    public Library(String name, String street, String city, String state) {
        this.name = name;
        this.street = street;
        this.city = city;
        this.state = state;
        this.shelves = new HashMap<Integer, Shelf>();
    }

    public void addBook(Book b, Integer shelfNum, Integer shelfFloor) {
        Shelf shelf = getOrCreateShelf(shelfNum, shelfFloor);
        shelf.books.add(b);
    }

    public Shelf getOrCreateShelf(Integer shelfNum, Integer shelfFloor) {
        Shelf shelf = shelves.get(shelfNum);
        if (shelf == null) {
            shelf = new Shelf(name, shelfNum, shelfFloor);
            shelves.put(shelfNum, shelf);
        }
        return shelf;
    }

    @Override
    public String toString() {
        return String.format("('%s','%s','%s','%s')", name, street, city, state);
    }
}
