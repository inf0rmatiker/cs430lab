import java.util.ArrayList;
import java.util.List;

public class Publisher {
    public Integer id;
    public String name;

    public List<Phone> phones;

    public Publisher(Integer id, String name) {
        this.id = id;
        this.name = name;
        this.phones = new ArrayList<>();
    }

    public void addPhone(Phone phone) {
        phones.add(phone);
    }

    @Override
    public String toString() {
        // (id,name)
        return String.format("(%d,'%s')", id, name);
    }
}
