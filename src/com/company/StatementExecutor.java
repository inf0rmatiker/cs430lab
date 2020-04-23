package com.company;

import java.sql.*;

public class StatementExecutor {

    private static final String USERNAME = System.getenv("SQLUSERNAME");
    private static final String PASSWORD = System.getenv("SQLPASSWORD");
    private static final String URL = "jdbc:mysql://faure/" + USERNAME;

    public static void executeStatements() {


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

            try{

                rs = stmt.executeQuery("SELECT * FROM author");
                while (rs.next()) {
                    System.out.println (rs.getString("author_id"));
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
}
