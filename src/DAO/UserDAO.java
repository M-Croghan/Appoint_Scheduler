package DAO;

import Model.Customer;
import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

/** User Data Access Object Class */
public class UserDAO {

    /** This method accepts a username and password parameters and checks whether the users credentials permits them to log-in and user the application.
     * @param name user's username
     * @param pw user's password
     * @return boolean depending on whether username and password match existing record in database.
     * @throws SQLException If the SQL statements within the SQL statement contain an error. */
    public static boolean validateUser(String name, String pw) throws SQLException {
        boolean authenticate = false;
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT * FROM users";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();

        while (rs.next()){
            String username = rs.getString("User_Name");
            String password = rs.getString("Password");

            if(username.equals(name) && password.equals(pw)){
                authenticate = true;
                break;
            }
            else{
                authenticate = false;
            }
        }
        return authenticate;
    }

    /** This method accepts a username as a parameter and returns the user ID associated with it in the User table of the database.
     * @param userName username
     * @return user's ID
     * @throws SQLException If the SQL statements within the SQL statement contain an error.*/
    public static int getUserID(String userName) throws SQLException {

        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT * FROM users WHERE User_Name = '" + userName + "'";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();

        if (rs.next()) {
            return Integer.parseInt(rs.getString(1));
        }
            return 0;
    }

    /** General purpose method to return a list of user names from a database.
     * @return a list of user names from the user table.
     * @throws SQLException in the event of SQL syntax or execution error. */
    public static ObservableList<User> getUserName() throws SQLException {

        ObservableList<User> allUsers = FXCollections.observableArrayList();
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT * FROM users";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();

        while (rs.next()){
            String userName = rs.getString("User_Name");

            User userResult = new User(userName);
            allUsers.add(userResult);
        }
        return allUsers;
    }

    /** Takes in user ID and returns the user's name associated with it.
     * @param userID user ID.
     * @return user name associated with ID
     * @throws SQLException in the event of SQL syntax or execution error. */
    public static String getUserName(int userID) throws SQLException {
        String currentUser = null;
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT * FROM users WHERE User_ID = '" + userID + "'";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();

        if(rs.next()){
            currentUser = rs.getString(2);
        }
        return currentUser;
    }

}
