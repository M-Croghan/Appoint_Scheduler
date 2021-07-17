package DAO;

import Model.Contacts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

/** Contacts Data Access Object Class.
 * Contains necessary CRUD methods for interacting with the database. */
public class ContactsDAO {

    /** General purpose method to return a list of contact names from a database.
     * @return a list of contact names from the contacts table.
     * @throws SQLException in the event of SQL syntax or execution error. */
    public static ObservableList<Contacts> getContactName() throws SQLException {
        ObservableList<Contacts> allContacts = FXCollections.observableArrayList();
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT * FROM contacts";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();

        while (rs.next()){
            String contactName = rs.getString("Contact_Name");


            Contacts contactResult = new Contacts(contactName);
            allContacts.add(contactResult);
        }

        return allContacts;
    }

    /** Takes in contact name and returns the contacts ID associated with it.
     * @param contactName contact name.
     * @return ID associated with the contact name.
     * @throws SQLException in the event of SQL syntax or execution error. */
    public static int getContactID(String contactName) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT * FROM contacts WHERE Contact_Name = '" + contactName + "'";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();

        if(rs.next()){
            return rs.getInt(1);
        }
        return 0;
    }

    /** Takes in contact ID and returns the contact's name associated with it.
     * @param contactId customer ID.
     * @return customer name associated with ID
     * @throws SQLException in the event of SQL syntax or execution error. */
    public static String getContactName(int contactId) throws SQLException {
        String currentContact = null;
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT * FROM contacts WHERE Contact_ID = '" + contactId + "'";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();

        if(rs.next()){
            currentContact = rs.getString(2);
        }
        return currentContact;
    }

}
