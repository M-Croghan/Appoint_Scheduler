package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static Model.WarningMessages.appLengthError;
import static Model.WarningMessages.invalidTimeError;

/** Establishes the creation and manipulation of Appointment objects */
public class Appointments {

    /** appointment ID property */
    private int appointmentID;

    /** appointment title property */
    private String title;

    /** appointment description property */
    private String description;

    /** appointment location property */
    private String location;

    /** appointment type property */
    private String type;

    /** appointment start time property */
    private String startTime;

    /** appointment end time property */
    private String endTime;

    /** appointment create date property */
    private Date createDate;

    /** appointment created by property */
    private String createdBy;

    /** appointment last update property */
    private Timestamp lastUpdate;

    /** appointment updated by property */
    private String updatedBy;

    /** appointment's associated customer ID property */
    private int customerID;

    /** appointment's associated user ID property */
    private int userID;

    /** appointment's associated contact ID property */
    private int contactID;

    /** Business hours start time */
    private static final LocalTime utcStart = LocalTime.of(12,0);
    /** Business hours end time */
    private static final LocalTime utcEnd = LocalTime.of(2,0);

    /** List which holds the types of appointments */
    public static final ObservableList<Object> appointmentTypes = FXCollections.observableArrayList("In-Person Interview", "In-Person Meeting", "Phone Interview", "Video Interview", "Video Meeting");

    /** Appointments Constructor */
    public Appointments(int appointmentID, String title, String description, String location, String type, String startTime, String endTime, Date createDate, String createdBy, Timestamp lastUpdate, String updatedBy, int customerID, int userID, int contactID) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.updatedBy = updatedBy;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
    }

    /** Empty appointments constructor */
    public Appointments() {

    }

    /** This method helps facilitate the conversion of an appointment time for database storage
     * @param datePick specified date
     * @param timeSpinner specified time
     * @return a string of the converted date and time. */
    public static String utcConversion(LocalDate datePick, LocalTime timeSpinner) {

        LocalDateTime userLDT = LocalDateTime.of(datePick, timeSpinner);

        //Create User's ZoneID
        ZoneId userZoneID = ZoneId.systemDefault();

        //Convert to ZonedDateTime
        ZonedDateTime userZDT = ZonedDateTime.of(userLDT, userZoneID);

        // Convert to UTC
        ZoneId utcZone = ZoneId.of("UTC");
        ZonedDateTime utcTime = ZonedDateTime.ofInstant(userZDT.toInstant(), utcZone);
        String convertedTime = utcTime.toLocalDate().toString() + " " + utcTime.toLocalTime().toString();

        return convertedTime;
    }

    /** Displays the business hours of opertions in the users time zone
     * @param lt the of business hours
     * @return the business hours converted to the users time*/
    public static LocalTime displayBusinessHours(LocalTime lt){
        ZoneId zone = ZoneId.of("UTC");
        ZonedDateTime zdt = ZonedDateTime.of(LocalDate.now(), lt, zone);
        ZoneId userZone = ZoneId.systemDefault();
        ZonedDateTime localHours = ZonedDateTime.ofInstant(zdt.toInstant(), userZone);
        return localHours.toLocalTime();
    }

    /** This method takes in a string of a date and time and converts it to the users time
     * @param datetime a string of date and time
     * @return a converted date and time in users zone*/
    public static String displayLocalTime(String datetime) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String[] s = datetime.split(" ");

        LocalDate d = LocalDate.parse(s[0]);
        LocalTime t = LocalTime.parse(s[1]);
        LocalDateTime extractedDateTime = LocalDateTime.of(d,t);
        ZoneId dbZoneId = ZoneId.of("UTC");
        ZonedDateTime storedDBTime = extractedDateTime.atZone(dbZoneId);

        ZoneId userZoneId = ZoneId.systemDefault();
        ZonedDateTime userDateTime = ZonedDateTime.ofInstant(storedDBTime.toInstant(), userZoneId);

        return userDateTime.format(format);
    }

    /** This method takes in a date and time as a string and then splits it in order to return a local date.
     * @param s date and time as a string
     * @return the date portion of the string returned as a local date */
    public static LocalDate dateSplitter(String s){
        String[] split = s.split(" ");
        LocalDate returnDate = LocalDate.parse(split[0]);
        return returnDate;
    }

    /** This method takes in a date and time as a string and then splits it in order to return a local time.
     * @param s date and time as a string
     * @return the time portion of the string returned as a local time */
    public static LocalTime timeSplitter(String s){
        String[] split = s.split(" ");
        LocalTime returnTime = LocalTime.parse(split[1]);
        return returnTime;
    }

    /** This method takes in a local date and time and created a LocalDateTime object
     * @param lt local time
     * @param ld local date
     * @return local date time object composed of the parameters passed in*/
    public static LocalDateTime ldtMaker(LocalDate ld, LocalTime lt){
        return LocalDateTime.of(ld, lt);
    }

    /** This method accepts a local time and converts it to display the scheduled time in the users timezone.
     * @param lt the local time
     * @return converted time for the user*/
    public static LocalTime displayUserTime(LocalTime lt){
        ZoneId zone = ZoneId.systemDefault();
        ZonedDateTime zdt = ZonedDateTime.of(LocalDate.now(), lt, zone);
        ZoneId userZone = ZoneId.systemDefault();
        ZonedDateTime localHours = ZonedDateTime.ofInstant(zdt.toInstant(), userZone);
        return localHours.toLocalTime();
    }

    /** This method when called performs a check on an appointment's start / end times to ensure it falls within the organization's hours of operation.
     * @param start the scheduled appointment start time.
     * @param end  the scheduled appointment end time
     * @return boolean on whether the selected dates are valid for scheduling */
    public static boolean dateCheck(LocalDateTime start, LocalDateTime end) throws SQLException {
        ZoneId userId = ZoneId.systemDefault();
        ZonedDateTime userStartTime = ZonedDateTime.of(start.toLocalDate(), start.toLocalTime(), userId);
        ZonedDateTime userEndTime = ZonedDateTime.of(end.toLocalDate(), end.toLocalTime(), userId);
        ZoneId utcZone = ZoneId.of("America/New_York");
        ZonedDateTime convertedStartTime = ZonedDateTime.ofInstant(userStartTime.toInstant(),utcZone);
        ZonedDateTime convertedEndTime = ZonedDateTime.ofInstant(userEndTime.toInstant(), utcZone);

        LocalTime businessStart = LocalTime.of(7,59);
        LocalTime businessEnd = LocalTime.of(22,1);
        ZonedDateTime initialStart = ZonedDateTime.of(start.toLocalDate(),businessStart, utcZone);
        ZonedDateTime initalEnd = ZonedDateTime.of(start.toLocalDate(), businessEnd, utcZone);
        ZonedDateTime closingStart = ZonedDateTime.of(end.toLocalDate(),businessStart, utcZone);
        ZonedDateTime closingEnd = ZonedDateTime.of(end.toLocalDate(), businessEnd, utcZone);

        if((convertedStartTime.toLocalTime().isBefore(initialStart.toLocalTime()) || convertedStartTime.toLocalTime().isAfter(initalEnd.toLocalTime())) ||
                (convertedEndTime.toLocalTime().isBefore(closingStart.toLocalTime()) || convertedEndTime.toLocalTime().isAfter(closingEnd.toLocalTime()))){
            Alert wrongTime = new Alert(Alert.AlertType.INFORMATION);
            wrongTime.setTitle("Scheduling Error!");
            wrongTime.setHeaderText("Scheduled time falls outside of business hours!");
            wrongTime.setContentText("Headquarters main hours of operation fall between " + displayBusinessHours(utcStart).toString() +
                    " - " + displayBusinessHours(utcEnd).toString() + " your time.");
            wrongTime.showAndWait();
            return false;

        }
        // 8:00AM - 10:00PM EST.... INCLUDING WEEKEND... SO 7 DAYS A WEEK
//        int daysOfWeek = start.getDayOfWeek().getValue();
//
//        if (daysOfWeek == 6 || daysOfWeek == 7){
//            isWeekendError();
//            return false;
//        }
        if (end.isBefore(start)){
            invalidTimeError();
            return false;
        }
        if(end.isAfter(start.plusHours(3))){
            appLengthError();
            return false;
        }


        return true;
    }


    /** Retrieves the appointment ID
     * @return appointment ID */
    public int getAppointmentID() {
        return appointmentID;
    }


    /** Accepts integer parameter and sets the appointment ID
     * @param appointmentID */
    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }


    /** Retrieves the title of the appointment
     * @return appointment title */
    public String getTitle() {
        return title;
    }


    /** Accepts string parameter and sets the appointment title
     * @param title */
    public void setTitle(String title) {
        this.title = title;
    }

    /** Retrieves the description of the appointment.
     * @return appointment description */
    public String getDescription() {
        return description;
    }


    /** Accepts string parameter and sets the appointment description.
     * @param description */
    public void setDescription(String description) {
        this.description = description;
    }

    /** Retrieves the location of the appointment
     * @return appointment location */
    public String getLocation() {
        return location;
    }

    /** Accepts string parameter and sets the appointment location.
     * @param location */
    public void setLocation(String location) {
        this.location = location;
    }

    /** Retrieves the type of the appointment
     * @return appointment type*/
    public String getType() {
        return type;
    }

    /** Accepts string parameter and sets the appointment type
     * @param type */
    public void setType(String type) {
        this.type = type;
    }

    /** Retrieves the start time of the appointment
     * @return appointment start time */
    public String getStartTime() {
        return startTime;
    }

    /** Accepts string parameter and sets when the appointment starts.
     * @param startTime */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /** Retrieves the end time of the appointment
     * @return appointment end time */
    public String getEndTime() {
        return endTime;
    }

    /** Accepts string parameter and sets when the appointment ends
     * @param endTime */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /** Retrieves the date the appointment was created
     * @return creation date of appointment*/
    public Date getCreateDate() {
        return createDate;
    }

    /** Accepts date parameter and sets when the appointment was created
     * @param createDate */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /** Retrieves username of who created the appointment
     * @return user's's name*/
    public String getCreatedBy() {
        return createdBy;
    }

    /** Accepts string parameter and sets who created the appointment
     * @param createdBy */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /** Retrieves a timestamp of last time appointment was updated
     * @return last update timestamp*/
    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    /** Accepts timestamp parameter and sets the time of last appointment update
     * @param lastUpdate */
    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /** Retrieves a who updated the appointment
     * @return updated by */
    public String getUpdatedBy() {
        return updatedBy;
    }

    /** Accepts string parameter and sets who last updated the appointment.
     * @param updatedBy */
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    /** Retrieves a customer ID
     * @return customer's ID*/
    public int getCustomerID() {
        return customerID;
    }

    /** Accepts integer parameter and sets customer's ID
     * @param customerID */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /** Retrieves a user ID
     * @return user's's ID*/
    public int getUserID() {
        return userID;
    }

    /** Accepts integer parameter and sets users's ID
     * @param userID */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /** Retrieves a contact ID
     * @return contacts's ID*/
    public int getContactID() {
        return contactID;
    }

    /** Accepts integer parameter and sets Contact's ID
     * @param contactID */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }
}
