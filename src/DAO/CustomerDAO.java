package DAO;

import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

/** Customer Data Access Object Class.
 * Contains necessary CRUD methods for interacting with the database. */
public class CustomerDAO {

    /** General purpose method to return a list of customer records from a database.
     * @return a list of customer records from the customer table.
     * @throws SQLException in the event of SQL syntax or execution error. */
    public static ObservableList<Customer> getCustomer() throws SQLException {

        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT * FROM customers";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();

        while (rs.next()){
            int customerID = rs.getInt("Customer_ID");
            String customerName = rs.getString("Customer_Name");
            String address = rs.getString("Address");
            String postal = rs.getString("Postal_Code");
            String phone = rs.getString("Phone");
            Date createDate = rs.getDate("Create_Date");
            String createdBy = rs.getString("Created_By");
            Timestamp lastUpdate = rs.getTimestamp("Last_Update");
            String updateBy = rs.getString("Last_Updated_By");
            int divID = rs.getInt("Division_ID");

            Customer customerResult = new Customer(customerID, customerName, address, postal, phone, createDate, createdBy, lastUpdate, updateBy, divID);
            allCustomers.add(customerResult);
        }
        return allCustomers;
    }

    /** General purpose method to return a list of customer names from a database.
     * @return a list of customer names from the customer table.
     * @throws SQLException in the event of SQL syntax or execution error. */
    public static ObservableList<Customer> getCustomerName() throws SQLException {

        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT * FROM customers";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();

        while (rs.next()){
            String customerName = rs.getString("Customer_Name");

            Customer customerResult = new Customer(customerName);
            allCustomers.add(customerResult);
        }
        return allCustomers;
    }

    /** Takes in customer name and returns the customers ID associated with it.
     * @param customerName customer's name.
     * @return ID associated with the name.
     * @throws SQLException in the event of SQL syntax or execution error. */
    public static int getCustomerID(String customerName) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT * FROM customers WHERE Customer_Name = '" + customerName + "'";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();

        if(rs.next()){
            return rs.getInt(1);
        }
        return 0;
    }

    /** Takes in customer ID and returns the customers name associated with it.
     * @param customerId customer ID.
     * @return customer name associated with ID
     * @throws SQLException in the event of SQL syntax or execution error. */
    public static String getCustomerName(int customerId) throws SQLException {
        String currentCustomer = "";
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT * FROM customers WHERE Customer_ID = '" + customerId + "'";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();

        if(rs.next()){
            currentCustomer = rs.getString(2);
        }
        return currentCustomer;
    }

    /** Takes in necessary customer fields and creates a new customer in the customer table within the database.
     * @throws SQLException in the event of SQL syntax or execution error. */
    public static void addCustomer(String name, String address, String zip, String phone, String createdBy, Timestamp lastUpdate, String updatedBy, int divID) throws SQLException {

        Connection conn = DBConnection.getConnection();
        String insertStatement = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Created_By, Last_Update, Last_Updated_By, Division_ID) VALUES (?,?,?,?,?,?,?,?)";
        DBQuery.setPreparedStatement(conn, insertStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();

        ps.setString(1, name);
        ps.setString(2, address);
        ps.setString(3, zip);
        ps.setString(4, phone);
        ps.setString(5, createdBy);
        ps.setTimestamp(6, lastUpdate);
        ps.setString(7, updatedBy);
        ps.setInt(8, divID);

        ps.execute();
    }

    /** Takes in necessary customer fields and updates the existing customer in the customer table within the database.
     * @throws SQLException in the event of SQL syntax or execution error. */
    public static void updateCustomer(int custID, String name, String address, String zip, String phone, Timestamp lastUpdate, String updatedBy, int divID) throws SQLException {

        Connection conn = DBConnection.getConnection();
        String selectStatement = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ?\n" +
                "WHERE Customer_ID = '" + custID + "'";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();

        ps.setString(1, name);
        ps.setString(2, address);
        ps.setString(3, zip);
        ps.setString(4, phone);
        ps.setTimestamp(5, lastUpdate);
        ps.setString(6, updatedBy);
        ps.setInt(7, divID);

        ps.execute();
    }

    /** Takes in a customer ID to include in the SQL statement to delete a record from the customer table.
     * @throws SQLException in the event of SQL syntax or execution error.*/
    public static void deleteCustomer(int customerID) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String deleteStatement = "DELETE FROM customers WHERE Customer_ID = '" + customerID + "'";
        DBQuery.setPreparedStatement(conn, deleteStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
    }
}
