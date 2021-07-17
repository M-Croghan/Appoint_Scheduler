package DAO;

import Model.Countries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Country Data Access Object Class.
 * Contains necessary CRUD methods for interacting with the database. */
public class CountriesDAO {

    /** Method for retrieving records from the country table where the country ID's match the regions in which
     * the business operates in (Canada - 38, United Kingdom - 230, United States - 231)
     * @return a list of country records from the country table.
     * @throws SQLException in the event of SQL syntax or execution error. */
    public static ObservableList<Countries> getCountries() throws SQLException {
        ObservableList<Countries> allCountries = FXCollections.observableArrayList();
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT * FROM countries WHERE Country_ID = 38 OR Country_ID = 230 OR Country_ID = 231";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();

        while (rs.next()){
            int countryID = rs.getInt("Country_ID");
            String countryName = rs.getString("Country");

            Countries countriesResult = new Countries(countryID, countryName);
            allCountries.add(countriesResult);
        }
        return allCountries;
    }

}
