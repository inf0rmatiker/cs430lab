import java.sql.*;

public class GuiExecutor {

    private final String USERNAME;
    private final String PASSWORD;
    private final String URL;

    private Connection connection;

    public GuiExecutor() throws IllegalStateException {
        USERNAME = System.getenv("SQLUSERNAME");
        PASSWORD = System.getenv("SQLPASSWORD");
        URL = "jdbc:mysql://faure/" + USERNAME + "?serverTimezone=UTC";

        if (USERNAME == null || USERNAME.isEmpty()) {
            if (PASSWORD == null || PASSWORD.isEmpty()) {
                throw new IllegalStateException("Must define system variables 'SQLUSERNAME' and 'SQLPASSWORD'");
            }
        }

        // Register the JDBC driver for MySQL
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Class not found:\n" + e.getMessage());
        }

        this.connection = null;

        // Establish connection
        try {
            DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // Display URL and connection information
            System.out.println("INFO: URL: " + URL);
            System.out.println("INFO: Connection: " + connection);
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to establish connection:\n:" + e.getMessage());
        }

    }

    public boolean memberExists(int memberId) {
        try {
            String memberQuery = String.format("SELECT * FROM member_id WHERE member_id = %d", memberId);

            ResultSet rs;
            Statement stmt = connection.createStatement();
            rs = stmt.executeQuery(memberQuery);

            return rs.next(); // Returns true if the new current row is valid; false if there are no more rows
        } catch (SQLException e) {
            System.err.println("ERROR: Problem creating statement from connection");
        }

        return false;
    }

}
