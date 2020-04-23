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
                        //processCheckoutRecord(stmt, record);
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

        try {
            // Check for record existing, then update record
            rs = stmt.executeQuery("SELECT * FROM borrowed_by AS b\n" +
                    "WHERE b.member_id = " + record.member_id + " AND\n" +
                    "b.isbn = " + record.isbn + "AND b.checkin_date IS NULL");
            if (rs.next()) {
                rs = stmt.executeQuery("UPDATE borrowed_by AS b\n" +
                        "SET checkin_date = " + record.checkin_date + "\n" +
                        "WHERE b.member_id = " + record.member_id + " AND\n" +
                        "b.isbn = " + record.isbn + "AND b.checkin_date IS NULL");
            }

        } catch (SQLException e) {
            System.out.println("No checkout record exists for book checkin attempt!");
        }


    }

    private static void processCheckoutRecord(Statement stmt, Checkout record) {
        ResultSet rs;


    }
}
