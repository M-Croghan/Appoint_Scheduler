package DAO;

import Model.Appointments;
import Model.WarningMessages;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import static Model.Appointments.displayLocalTime;
import static View_Controller.AppointmentController.selectedAppointment;


/** Appointments Data Access Object Class.
 * Contains necessary CRUD methods for interacting with the database. */
public class AppointmentsDAO {

    /**
     * General purpose method to return a list of appointment records from a database.
     *
     * @return a list of appointments records from the appointments table.
     * @throws SQLException   in the event of SQL syntax or execution error.
     * @throws ParseException if unable to translate dates or times in table views
     */
    public static ObservableList<Appointments> getAppointments() throws SQLException, ParseException {

        ObservableList<Appointments> allAppointments = FXCollections.observableArrayList();
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT * FROM appointments";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();

        while (rs.next()) {
            int appointmentID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            String startDateTime = displayLocalTime(rs.getString("Start"));
            String endDateTime = displayLocalTime(rs.getString("End"));
            Date createdDate = rs.getDate("Create_Date");
            String createdBy = rs.getString("Created_By");
            Timestamp lastUpdate = rs.getTimestamp("Last_Update");
            String updateBy = rs.getString("Last_Updated_By");
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");
            int contactID = rs.getInt("Contact_ID");

            Appointments appointmentResult = new Appointments(appointmentID, title, description, location, type, startDateTime, endDateTime, createdDate, createdBy, lastUpdate, updateBy, customerID, userID, contactID);
            allAppointments.add(appointmentResult);
        }
        return allAppointments;
    }

    /**
     * Takes in required appointment fields and creates a new appointment in the appointments table within the database.
     *
     * @throws SQLException in the event of SQL syntax or execution error.
     */
    public static void addAppointment(String title, String description, String location, String type, String startTime, String endTime, String createdBy, String updatedBy, int custId, int userId, int contactId) throws SQLException {

        Connection conn = DBConnection.getConnection();
        String insertStatement = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Created_By, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        DBQuery.setPreparedStatement(conn, insertStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();

        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setString(5, startTime);
        ps.setString(6, endTime);
        ps.setString(7, createdBy);
        ps.setString(8, updatedBy);
        ps.setInt(9, custId);
        ps.setInt(10, userId);
        ps.setInt(11, contactId);

        ps.execute();
    }


    /**
     * Takes in an appointment ID to include in the SQL statement to delete a record from the customer table.
     *
     * @throws SQLException in the event of SQL syntax or execution error.
     */
    public static void deleteAppointment(int appID) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String deleteStatement = "DELETE FROM appointments WHERE Appointment_ID = '" + appID + "'";
        DBQuery.setPreparedStatement(conn, deleteStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
    }

    /**
     * Takes in required appointment fields and updates the existing appointment in the appointments table within the database.
     *
     * @throws SQLException in the event of SQL syntax or execution error.
     */
    public static void editAppointment(int appointID, String title, String description, String location, String type, String startTime, String endTime, String createdBy, String updatedBy, int custId, int userId, int contactId) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String updateStatement = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Created_By = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = '" + appointID + "'";

        DBQuery.setPreparedStatement(conn, updateStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();

        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setString(5, startTime);
        ps.setString(6, endTime);
        ps.setString(7, createdBy);
        ps.setString(8, updatedBy);
        ps.setInt(9, custId);
        ps.setInt(10, userId);
        ps.setInt(11, contactId);


        ps.execute();
    }

    /**
     * This method queries the database and returns a list of any appointment conflicts with the one attempting to be added / updated.
     *
     * @param startTime the start time of the appointment.
     * @param endTime   the start time of the appointment.
     * @return list of appointment records that have conflict with the scheduled time.
     * @throws SQLException in the event of SQL syntax or execution error.
     */
    public static ObservableList<Appointments> findTimeConflict(LocalDateTime startTime, LocalDateTime endTime) throws SQLException {
        ObservableList<Appointments> conflictingAppointments = FXCollections.observableArrayList();
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT * FROM appointments WHERE (start >= ? AND end <= ?) OR (start <= ? AND end >= ?) OR (start BETWEEN ? AND ? OR end BETWEEN ? AND ?)";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ZoneId userZone = ZoneId.systemDefault();
        ZoneId utcZone = ZoneId.of("UTC");
        LocalDateTime start = startTime.atZone(userZone).withZoneSameInstant(utcZone).toLocalDateTime();
        LocalDateTime end = endTime.atZone(userZone).withZoneSameInstant(utcZone).toLocalDateTime();

        ps.setTimestamp(1, Timestamp.valueOf(start));
        ps.setTimestamp(2, Timestamp.valueOf(end));
        ps.setTimestamp(3, Timestamp.valueOf(start));
        ps.setTimestamp(4, Timestamp.valueOf(end));
        ps.setTimestamp(5, Timestamp.valueOf(start));
        ps.setTimestamp(6, Timestamp.valueOf(end));
        ps.setTimestamp(7, Timestamp.valueOf(start));
        ps.setTimestamp(8, Timestamp.valueOf(end));
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int appointmentID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            String startDateTime = displayLocalTime(rs.getString("Start"));
            String endDateTime = displayLocalTime(rs.getString("End"));
            Date createdDate = rs.getDate("Create_Date");
            String createdBy = rs.getString("Created_By");
            Timestamp lastUpdate = rs.getTimestamp("Last_Update");
            String updateBy = rs.getString("Last_Updated_By");
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");
            int contactID = rs.getInt("Contact_ID");

            Appointments appointmentResult = new Appointments(appointmentID, title, description, location, type, startDateTime, endDateTime, createdDate, createdBy, lastUpdate, updateBy, customerID, userID, contactID);
            conflictingAppointments.add(appointmentResult);
        }
        return conflictingAppointments;
    }

    public static boolean overlappedTime(LocalDateTime start, LocalDateTime end) throws SQLException {
        ObservableList<Appointments> conflictingAppointments = findTimeConflict(start, end);
        if (conflictingAppointments.size() < 1) {
            return false;
        }
        else{
            for(Appointments badAppt : conflictingAppointments){
                if(selectedAppointment != null){
                    if(selectedAppointment.getStartTime().equals(badAppt.getStartTime())){
                        if(selectedAppointment.getEndTime().equals(badAppt.getEndTime())){
                            return false;
                        }
                    }
                }

                if(!badAppt.getEndTime().equals(start.toLocalTime().toString()) || !badAppt.getStartTime().equals(end.toLocalTime().toString())){
                    return true;
                }
            }
        }


        return true;
    }

    /** Queries the database to search for any appointments upcoming within the next 15 minutes.
     * @return a list of appointments that  are upcoming in the next 15 minute interval.
     * @throws SQLException in the event of SQL syntax or execution error.*/
    public static ObservableList<Appointments> upcomingAppointments() throws SQLException {
        ObservableList<Appointments> upcommingApps = FXCollections.observableArrayList();
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT * FROM appointments WHERE Start BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 15 MINUTE)";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.executeQuery();
        ResultSet rs = ps.getResultSet();

        while (rs.next()){
            int appointmentID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            String startDateTime = rs.getString("Start");
            String endDateTime = rs.getString("End");
            Date createdDate =  rs.getDate("Create_Date");
            String createdBy = rs.getString("Created_By");
            Timestamp lastUpdate = rs.getTimestamp("Last_Update");
            String updateBy = rs.getString("Last_Updated_By");
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");
            int contactID = rs.getInt("Contact_ID");

            Appointments appointmentResult = new Appointments(appointmentID, title, description, location, type, startDateTime,endDateTime, createdDate, createdBy, lastUpdate, updateBy, customerID, userID, contactID);
            upcommingApps.add(appointmentResult);
        }

        return upcommingApps;
    }



    /** Method queries the database to search for appointments by type. Takes in parameters to concatenate a string for use as SQL statement.
     * @param filter the type of search a user wants to execute based on an integer ID (contact / customer).
     * @param criteria narrows the search by a specific customer / contact / type.
     * @param timeframe concatenates the SQL statement for a specific interval to search from.
     * @return a list of appointment records based on the query.
     * @throws SQLException in the event of SQL syntax or execution error.*/
    public static ObservableList<Appointments> getAppointmentsByType(String filter, int criteria, String timeframe) throws SQLException {

        ObservableList<Appointments> allAppointments = FXCollections.observableArrayList();
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT * FROM appointments WHERE " + filter + " = " + criteria  + timeframe;
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();

        while (rs.next()) {
            int appointmentID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            String startDateTime = displayLocalTime(rs.getString("Start"));
            String endDateTime = displayLocalTime(rs.getString("End"));
            Date createdDate = rs.getDate("Create_Date");
            String createdBy = rs.getString("Created_By");
            Timestamp lastUpdate = rs.getTimestamp("Last_Update");
            String updateBy = rs.getString("Last_Updated_By");
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");
            int contactID = rs.getInt("Contact_ID");

            Appointments appointmentResult = new Appointments(appointmentID, title, description, location, type, startDateTime, endDateTime, createdDate, createdBy, lastUpdate, updateBy, customerID, userID, contactID);
            allAppointments.add(appointmentResult);
        }

        return allAppointments;
    }


    /** Method queries the database to search for appointments by time. Takes in parameter to concatenate a string for use with SQL statement.
      * @param timeframe concatenates the SQL statement for a specific interval to search from.
     * @return a list of appointment records based on the timeframe of the query.
     * @throws SQLException in the event of SQL syntax or execution error.*/
    public static ObservableList<Appointments> getAppointmentsByTime(String timeframe) throws SQLException {

        ObservableList<Appointments> allAppointments = FXCollections.observableArrayList();
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT * FROM appointments " + timeframe;
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();

        while (rs.next()) {
            int appointmentID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            String startDateTime = displayLocalTime(rs.getString("Start"));
            String endDateTime = displayLocalTime(rs.getString("End"));
            Date createdDate = rs.getDate("Create_Date");
            String createdBy = rs.getString("Created_By");
            Timestamp lastUpdate = rs.getTimestamp("Last_Update");
            String updateBy = rs.getString("Last_Updated_By");
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");
            int contactID = rs.getInt("Contact_ID");

            Appointments appointmentResult = new Appointments(appointmentID, title, description, location, type, startDateTime, endDateTime, createdDate, createdBy, lastUpdate, updateBy, customerID, userID, contactID);
            allAppointments.add(appointmentResult);
        }

        return allAppointments;
    }

    /** Method queries the database to search for appointments by type. Takes in parameters to concatenate a string for use with SQL statement.
     * @param filter the type of search a user wants to execute based on a string input.
     * @param criteria narrows the search by a specific customer / contact / type.
     * @param timeframe concatenates the SQL statement for a specific interval to search from.
     * @return a list of appointment records based on the query.
     * @throws SQLException in the event of SQL syntax or execution error.*/
    public static ObservableList<Appointments> getAppointmentsByName(String filter, String criteria, String timeframe) throws SQLException {

        ObservableList<Appointments> allAppointments = FXCollections.observableArrayList();
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT * FROM appointments WHERE " + filter + " = '" + criteria + "'" + timeframe;
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();

        ResultSet rs = ps.getResultSet();

        while (rs.next()) {
            int appointmentID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            String startDateTime = displayLocalTime(rs.getString("Start"));
            String endDateTime = displayLocalTime(rs.getString("End"));
            Date createdDate = rs.getDate("Create_Date");
            String createdBy = rs.getString("Created_By");
            Timestamp lastUpdate = rs.getTimestamp("Last_Update");
            String updateBy = rs.getString("Last_Updated_By");
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");
            int contactID = rs.getInt("Contact_ID");

            Appointments appointmentResult = new Appointments(appointmentID, title, description, location, type, startDateTime, endDateTime, createdDate, createdBy, lastUpdate, updateBy, customerID, userID, contactID);
            allAppointments.add(appointmentResult);
        }

        return allAppointments;
    }



}
