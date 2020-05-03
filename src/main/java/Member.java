import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        if (isISODatePattern(DOB)) {
            this.DOB = DOB; // No need to make changes
        }
        else if (isNormalDatePattern(DOB)) {
            this.DOB = isoDateFormat(DOB); // Parse into appropriate format
        }
        else {
            this.DOB = "N/A";
        }

        this.gender = gender;
        this.checkouts = new ArrayList<Checkout>();
    }

    private boolean isISODatePattern(String dob) {
        Pattern format = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
        Matcher matcher = format.matcher(dob);
        return matcher.matches();
    }

    private boolean isNormalDatePattern(String dob) {
        Pattern format = Pattern.compile("^\\d{2}/\\d{2}/\\d{4}$");
        Matcher matcher = format.matcher(dob);
        return matcher.matches();
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
