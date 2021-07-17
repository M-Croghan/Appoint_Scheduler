package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Database Connection Class*/
public class DBConnection {

    // JDBC URL Parts
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//wgudb.ucertify.com/WJ08JmY";

    // JDBC URL
    private static final String jdbcURL = protocol + vendorName + ipAddress;

    // Driver & Connection Interface Reference
    private static final String MYSQLJDBCDriver = "com.mysql.cj.jdbc.Driver";
    private static Connection conn = null;

    // Database Login Credentials
    private static final String username = "U08JmY";
    private static final String password = "53689305338";

    /** This method takes in the pessary components for creating a connection to the MySQL database.
     * @return the started connection to the database.
     * Addressed SQLException in the event of syntax or execution error.*/
    public static Connection startConnection(){
        try{

            Class.forName(MYSQLJDBCDriver);
            conn = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Database Connection: Successful");
        }
        catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
        return conn;
    }

    /** This method when called terminated / closes the connection to the database.
     * Addressed SQLException in the event of syntax or execution error. */
    public static void closeConnection(){
        try{
            conn.close();
            System.out.println("Database Connection: Terminated");
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    /** retrieves the established connection */
    public static Connection getConnection(){
        return conn;
    }

}
