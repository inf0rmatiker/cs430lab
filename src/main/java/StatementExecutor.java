import java.sql.*;
import java.util.List;

public class StatementExecutor {

    private static final String USERNAME = System.getenv("SQLUSERNAME");
    private static final String PASSWORD = System.getenv("SQLPASSWORD");
    private static final String URL = "jdbc:mysql://faure/" + USERNAME + "?serverTimezone=UTC";

    public static void executeStatements(List<Checkout> checkouts) {
        if (USERNAME == null || USERNAME.isEmpty()) {
            if (PASSWORD == null || PASSWORD.isEmpty()) {
                System.err.println("Must define system variables 'SQLUSERNAME' and 'SQLPASSWORD'");
                System.exit(1);
            }
        }

        Connection con = null;
        try {
            Statement stmt;
            ResultSet rs;

            // Register the JDBC driver for MySQL.
            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // Display URL and connection information
            System.out.println("URL: " + URL);
            System.out.println("Connection: " + con);

            // Get a Statement object
            stmt = con.createStatement();

            try{ // Add checkout/checkin records to table

                for (Checkout record: checkouts) {
                    if (record.isCheckin()) {
                        processCheckinRecord(stmt, record);
                    }
                    else {
                        processCheckoutRecord(stmt, record);
                    }
                }

            } catch(Exception e) {
                System.out.print(e);
                System.out.println("No Author table to query");
            }//end catch

            con.close();
        }catch( Exception e ) {
            e.printStackTrace();

        }//end catch
    }

    private static void processCheckinRecord(Statement stmt, Checkout record) {
        ResultSet rs;
        System.out.println("Attempting to check in: " + record);

        try {
            // Check for record existing, then update record
            String query = "SELECT * FROM borrowed_by AS b\n" +
                    "WHERE b.member_id = " + record.member_id + " AND\n" +
                    "b.isbn = '" + record.isbn + "' AND b.checkin_date IS NULL";
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                // Update borrowed_by checkin date
                stmt.executeUpdate("UPDATE borrowed_by AS b\n" +
                        "SET checkin_date = " + record.checkin_date + "\n" +
                        "WHERE b.member_id = " + record.member_id + " AND\n" +
                        "b.isbn = '" + record.isbn + "' AND b.checkin_date IS NULL");

                // Increment library book count to first library that is storing book
                rs = stmt.executeQuery("SELECT * FROM stored_on AS so\n" +
                        "WHERE so.isbn = '" + record.isbn + "'");
                if (rs.next()) {
                    String firstLibraryName = rs.getString("name");
                    if (firstLibraryName.equals("Main") || firstLibraryName.equals("South Park")) {
                        String updateQuery = "UPDATE stored_on AS so\n" +
                                "SET so.total_copies = so.total_copies + 1\n" +
                                "WHERE so.isbn = '" + record.isbn + "' AND so.name = '" + firstLibraryName + "'";
                        if (stmt.executeUpdate(updateQuery) > 0) {
                            System.out.println("Book checkin successful.");
                        }
                    }
                    else {
                        System.out.println("Incorrect library name: " + firstLibraryName + ", must be either Main or South Park!");
                    }
                }
                else {
                    System.out.println("No libraries found to be storing that book!");
                }
            }
            else {
                System.out.println("No checkout record exists for book checkin attempt!");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("No checkout record exists for book checkin attempt!");
        }


    }

    private static void processCheckoutRecord(Statement stmt, Checkout record) {
        ResultSet rs;
        System.out.println("Attempting to check out: " + record);

        try {
            String query = "SELECT * FROM stored_on AS so\n" +
                    "WHERE so.isbn = '" + record.isbn + "' AND so.total_copies > 0";
            rs = stmt.executeQuery(query);

            if (rs.next()) {
                String firstLibraryName = rs.getString("name");

                // Decrement number of copies stored at that library
                if (firstLibraryName.equals("Main") || firstLibraryName.equals("South Park")) {
                    stmt.executeUpdate("UPDATE stored_on AS so\n" +
                            "SET so.total_copies = so.total_copies - 1\n" +
                            "WHERE so.isbn = '" + record.isbn + "' AND so.name = '" + firstLibraryName + "'");
                }
                else {
                    System.out.println("Incorrect library name: " + firstLibraryName + ", must be either Main or South Park!");
                }

                // Add checkout record to borrowed_by
                String insertQuery = "INSERT INTO borrowed_by (member_id, isbn, checkout_date, checkin_date)\n" +
                        "VALUES (" + record.member_id + ", '" + record.isbn + "', '" + record.checkout_date + "', NULL)";
                if (stmt.execute(insertQuery)) {
                    System.out.println("Book checkout successful.");
                };
            }
            else {
                System.out.println("That book is not currently available in either Main or South Park libraries.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Book is unavailable to be checked out!");
        }

    }
}
