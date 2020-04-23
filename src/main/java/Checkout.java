public class Checkout {

    public String isbn, checkout_date, checkin_date;

    public Checkout(String isbn, String checkout_date, String checkin_date) {
        this.isbn = isbn;
        this.checkout_date = isoDateFormat(checkout_date);
        if (checkin_date == null) {
            this.checkin_date = "NULL";
        }
        else {
            this.checkin_date = "'" + isoDateFormat(checkin_date) + "'" ;
        }
    }

    public String isoDateFormat(String date) {
        String[] parts = date.split("/");
        return String.format("%s-%s-%s", parts[2], parts[0], parts[1]);
    }

}
