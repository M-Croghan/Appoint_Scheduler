package DAO;


import Model.Division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Division Data Access Object Class */
public class DivisionDAO {

    /** This method takes in a division ID and returns a list of division information relating countries / divisions / states/ provinces
     *  @param divID division ID to be searched in the database.
     *  @return list a records in the database containing that division ID.
     *  @throws SQLException in response to an error found in SQL syntax / query execution. */
    public static ObservableList<Division> getDivision(int divID) throws SQLException {
        ObservableList<Division> allDivisions = FXCollections.observableArrayList();
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT * FROM first_level_divisions WHERE Country_ID = " + divID;
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();

        while (rs.next()){
            int countryID = rs.getInt("Country_ID");
            int divisionID = rs.getInt("Division_ID");
            String division = rs.getString("Division");

            Division divisionResult = new Division(countryID, divisionID, division);
            allDivisions.add(divisionResult);
        }
        return allDivisions;
    }

//    public static ObservableList<Division> getCanadaDivision() throws SQLException {
//        ObservableList<Division> canadaDivisions = FXCollections.observableArrayList();
//        Connection conn = DBConnection.getConnection();
//        String selectStatement = "SELECT * FROM first_level_divisions WHERE Country_ID = 38";
//        DBQuery.setPreparedStatement(conn, selectStatement);
//        PreparedStatement ps = DBQuery.getPreparedStatement();
//        ps.execute();
//        ResultSet rs = ps.getResultSet();
//
//        while (rs.next()){
//            int countryID = rs.getInt("Country_ID");
//            int divisionID = rs.getInt("Division_ID");
//            String division = rs.getString("Division");
//
//            Division divisionResult = new Division(countryID, divisionID, division);
//            canadaDivisions.add(divisionResult);
//        }
//        return canadaDivisions;
//    }

//    public static ObservableList<Division> getUKDivision() throws SQLException {
//        ObservableList<Division> ukDivisions = FXCollections.observableArrayList();
//        Connection conn = DBConnection.getConnection();
//        String selectStatement = "SELECT * FROM first_level_divisions WHERE Country_ID = 230";
//        DBQuery.setPreparedStatement(conn, selectStatement);
//        PreparedStatement ps = DBQuery.getPreparedStatement();
//        ps.execute();
//        ResultSet rs = ps.getResultSet();
//
//        while (rs.next()){
//            int countryID = rs.getInt("Country_ID");
//            int divisionID = rs.getInt("Division_ID");
//            String division = rs.getString("Division");
//
//            Division divisionResult = new Division(countryID, divisionID, division);
//            ukDivisions.add(divisionResult);
//        }
//        return ukDivisions;
//    }
//
//    public static ObservableList<Division> getUSDivision() throws SQLException {
//        ObservableList<Division> usDivisions = FXCollections.observableArrayList();
//        Connection conn = DBConnection.getConnection();
//        String selectStatement = "SELECT * FROM first_level_divisions WHERE Country_ID = 231";
//        DBQuery.setPreparedStatement(conn, selectStatement);
//        PreparedStatement ps = DBQuery.getPreparedStatement();
//        ps.execute();
//        ResultSet rs = ps.getResultSet();
//
//        while (rs.next()){
//            int countryID = rs.getInt("Country_ID");
//            int divisionID = rs.getInt("Division_ID");
//            String division = rs.getString("Division");
//
//            Division divisionResult = new Division(countryID, divisionID, division);
//            usDivisions.add(divisionResult);
//        }
//        return usDivisions;
//    }


    /** This method takes in a division name and returns the division ID associated with it.
     *  @param divisionName division name to be searched in the database.
     *  @return the division ID in the database related to the division name.
     *  @throws SQLException in response to an error found in SQL syntax / query execution. */
    public static int getDivisionID(String divisionName) throws SQLException {

        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT * FROM first_level_divisions WHERE Division = '" + divisionName + "'";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();

        if(rs.next()){
            return Integer.parseInt(rs.getString(1));
        }
        return 0;
    }

    /** This method takes in a division ID and returns a division name.
     *  @param divisionID division ID to be searched in the database.
     *  @return division name in the database related to that division ID.
     *  @throws SQLException in response to an error found in SQL syntax / query execution. */
    public static String getDivName(int divisionID) throws SQLException{
        String currentDiv = null;
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT * FROM first_level_divisions WHERE Division_ID = '" + divisionID + "'";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();

        if(rs.next()){
            currentDiv = rs.getString(2);
        }
        return currentDiv;
    }

    /** This method takes in a division ID and returns a country name.
     *  @param divID division ID to be searched in the database.
     *  @return country name in the database related to that division ID.
     *  @throws SQLException in response to an error found in SQL syntax / query execution. */
    public static String getCountryName(int divID) throws SQLException {
        String currentCountry = null;
        int countryID;
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT * FROM first_level_divisions WHERE Division_ID = '" + divID + "'";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();

        if(rs.next()){
            countryID = rs.getInt(7);
            if(countryID == 38){
                currentCountry = "Canada";
            }
            else if(countryID == 230){
                currentCountry = "United Kingdom";
            }
            else if(countryID == 231){
                currentCountry = "United States";
            }
        }
        return currentCountry;
    }

}
