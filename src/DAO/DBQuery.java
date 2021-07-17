package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/** Database Query Class */
public class DBQuery {

    /**Statement reference*/
    private static PreparedStatement statement;

    /** This method sets a prepared statement
     * @param conn the connection
     * @param sqlStatement sql statement to execute*/
    public static void setPreparedStatement(Connection conn, String sqlStatement){
        try {
            statement = conn.prepareStatement(sqlStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Returns the prepared statement that was set
     * @return prepared statement*/
    public static PreparedStatement getPreparedStatement(){
        return statement;
    }


}
