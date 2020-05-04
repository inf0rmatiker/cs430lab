import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GuiExecutor {

    private final String USERNAME;
    private final String PASSWORD;
    private final String URL;

    private Connection connection;

    private Member member;

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
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // Display URL and connection information
            System.out.println("INFO: URL: " + URL);
            System.out.println("INFO: Connection: " + connection.getClientInfo());
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to establish connection:\n:" + e.getMessage());
        }

    }

    public String getMemberFirstName() {
        return member.firstName;
    }

    public String getMemberLastName() {
        return member.lastName;
    }

    public boolean memberExists(int memberId) {
        String memberQuery = "";
        try {
            memberQuery = String.format("SELECT * FROM member WHERE member_id = %d", memberId);

            ResultSet rs;
            Statement stmt = connection.createStatement();
            rs = stmt.executeQuery(memberQuery);

            return rs.next(); // Returns true if the new current row is valid; false if there are no more rows
        } catch (SQLException e) {
            System.err.println("ERROR: Unable to execute query \"" + memberQuery + ";\"");
        }

        return false;
    }

    public void setMember(int memberId) {
        try {
            String memberQuery = String.format("SELECT * FROM member WHERE member_id = %d", memberId);

            ResultSet rs;
            Statement stmt = connection.createStatement();
            rs = stmt.executeQuery(memberQuery);

            if (rs.next()) {
                String firstName = rs.getString("first_name");
                String lastName  = rs.getString("last_name");
                String DOB       = rs.getString("dob");
                Character gender = rs.getString("gender").charAt(0);
                this.member = new Member(memberId, firstName, lastName, DOB, gender);
            }
        } catch (SQLException e) {
            System.err.println("ERROR: Problem creating statement from connection");
        }
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public boolean addMemberToDatabase() throws IllegalStateException {
        try {
            ResultSet rs;

            // Create member insertion query
            String addMemberUpdate = String.format("INSERT INTO member (member_id, first_name, last_name, dob, gender)\n" +
                    "VALUES (%d, '%s', '%s', '%s', '%s')", member.id, member.firstName, member.lastName, member.DOB, member.gender);
            Statement stmt = connection.createStatement();
            stmt.execute(addMemberUpdate);

            return memberExists(member.id);

        } catch (SQLException e) {
            throw new IllegalStateException("Unable to add member to database.");
        }


    }

    private boolean isValidIsbn(String isbn) {
        if (isbn == null || isbn.isEmpty()) return false;
        Pattern format = Pattern.compile("^\\d{2}-\\d{5}-\\d{5}$");
        Matcher matcher = format.matcher(isbn);
        return matcher.matches();
    }

    private boolean isValidTitleSubset(String title){
        return title != null && !title.isEmpty();
    }

    private boolean isValidAuthorName(String authorName) {
        if (authorName == null || authorName.isEmpty()) return false;
        String[] parts = authorName.trim().split(" ");
        return parts.length == 2;
    }

    private ResultSet searchByIsbn(String isbn) {
        try {
            String searchQuery = String.format("SELECT * FROM book WHERE isbn = '%s'", isbn);
            Statement stmt = connection.createStatement();
            return stmt.executeQuery(searchQuery);

        } catch (SQLException e) {
            throw new IllegalStateException("Unable to search for book by isbn.");
        }

    }

    private ResultSet searchByTitle(String title) {
        try {
            String searchQuery = "SELECT * FROM book WHERE title LIKE '%" + title + "%'";
            Statement stmt = connection.createStatement();
            return stmt.executeQuery(searchQuery);
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to search for book by title.");
        }

    }

    private ResultSet searchByAuthor(String author) {
        String[] parts = author.trim().split(" ");
        String firstName = parts[0];
        String lastName = parts[1];

        try {
            String searchQuery = "SELECT * FROM book AS b\n" +
                    "INNER JOIN (SELECT isbn FROM written_by AS wb\n" +
                    "INNER JOIN author AS a\n" +
                    "ON wb.author_id = a.author_id\n" +
                    "WHERE a.first_name = '" + firstName + "' AND a.last_name = '" + lastName + "') AS res_0\n" +
                    "ON b.isbn = res_0.isbn";

            Statement stmt = connection.createStatement();
            return stmt.executeQuery(searchQuery);
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to search for book by author.");
        }
    }


    public List<Book> getBooks(String isbnInput, String titleInput, String authorInput) {
        List<Book> books = new ArrayList<>();

        try {
            ResultSet rs = null;
            if (isValidIsbn(isbnInput)) {
                // Search by ISBN
                rs = searchByIsbn(isbnInput);
            }
            else if (isValidTitleSubset(titleInput)) {
                rs = searchByTitle(titleInput);
            }
            else if (isValidAuthorName(authorInput)) {
                rs = searchByAuthor(authorInput);
            }

            if (rs != null) {
                while (rs.next()) {
                    String isbn    = rs.getString("isbn");
                    String title   = rs.getString("title");
                    Integer year   = Integer.parseInt(rs.getString("year_published"));
                    Integer pub_id = Integer.parseInt(rs.getString("pub_id"));
                    books.add(new Book(isbn, title, year, pub_id, -1));
                }
            }

        } catch (SQLException e) {
            System.err.println("Unable to get book descriptions");
            System.err.println(e.getMessage());
        }

        return books;
    }

    public String findBook(Book book) {
        try {
            String searchQuery = "SELECT * FROM book AS b\n" +
                    "INNER JOIN\n" +
                    "  (SELECT * FROM stored_on AS so\n" +
                    "   WHERE so.total_copies > 0) AS instock\n" +
                    "ON b.isbn = instock.isbn\n" +
                    "WHERE b.isbn = '" + book.isbn + "'";

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(searchQuery);


            int count = 0;
            StringBuilder sb = new StringBuilder("");
            while (rs.next()) {
                String shelfNumber = rs.getString("s_num");
                String libraryName = rs.getString("name");
                sb.append(String.format("Book stored at %s Library on shelf %s.\n", libraryName, shelfNumber));
                count++;
            }

            if (count == 0) {
                sb.append("All copies are currently checked out.");
            }
            return sb.toString();

        } catch (SQLException e) {
            throw new IllegalStateException("Unable to search for book by author.");
        }
    }



}
