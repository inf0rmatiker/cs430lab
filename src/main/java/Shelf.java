import java.util.ArrayList;
import java.util.List;

public class Shelf {

    public String libraryName;
    public Integer s_num;
    public Integer s_floor;

    List<Book> books;

    public Shelf(String libraryName, Integer s_num, Integer s_floor) {
        this.libraryName = libraryName;
        this.s_num = s_num;
        this.s_floor = s_floor;
        this.books = new ArrayList<Book>();
    }

    @Override
    public String toString() {
        return String.format("('%s',%d,%d)", libraryName, s_num, s_floor);
    }
}
