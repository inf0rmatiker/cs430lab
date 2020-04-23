public class Checkout {

    public Integer member_id;
    public String  isbn, checkout_date, checkin_date;

    public Checkout(Integer member_id, String isbn, String checkout_date, String checkin_date) {
        this.member_id = member_id;
        this.isbn = isbn;
        if (checkout_date == null) {
            this.checkout_date = "NULL";
        }
        else {
            this.checkout_date = "'" + isoDateFormat(checkout_date) + "'";
        }
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

    public boolean isCheckin() {
        return checkout_date.equals("'NULL'");
    }

    public boolean isCheckout() {
        return !isCheckin();
    }

    @Override
    public String toString() {
        return String.format("(%d,'%s',%s,%s)",member_id,isbn,checkout_date,checkin_date);
    }

}
