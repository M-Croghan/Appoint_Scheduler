package View_Controller;

import DAO.AppointmentsDAO;
import DAO.ContactsDAO;
import DAO.CustomerDAO;
import Model.Contacts;
import Model.Customer;
import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.*;
import java.util.ResourceBundle;

import static DAO.ContactsDAO.getContactID;
import static DAO.CustomerDAO.getCustomerID;

import static DAO.UserDAO.getUserID;
import static DAO.UserDAO.getUserName;
import static Model.Appointments.*;
import static Model.WarningMessages.*;
import static View_Controller.LoginController.getCurrentUser;
import static View_Controller.LoginController.getCurrentUserID;
import static View_Controller.ScreenChange.*;

/** Controller class for creating new appointments */
public class NewAppointmentController implements Initializable {

    /** Text field which displays appointment ID. This field is disabled / uneditable.
    Default 'auto-generated' message  */
    @FXML public TextField appIDtext;

    /** Text field which collects appointment title */
    @FXML public TextField titleText;

    /** Text field which collects appointment location */
    @FXML public TextField locationText;

    /** Text field which collects appointment description */
    @FXML public TextArea descripText;

    /** Combo box which lists contact names to associate with an appointment */
    @FXML public ComboBox contactNameCombo;

    /** Combo box which lists customer names to associate with an appointment */
    @FXML public ComboBox customerNameCombo;

    /** Combo box which lists appointment type to associate with an appointment */
    @FXML public ComboBox appTypeCombo;

    /** Combo box which lists User to associate with an appointment */
    @FXML public ComboBox userCombo;

    /** Date picker allows for the selection of the start date of the appointment */
    @FXML public DatePicker startDatePick;

    /** Date picker allows for the selection of the end date of the appointment */
    @FXML public DatePicker endDatePick;

    /** Time Spinner allows for the selection of the start time of the appointment */
    @FXML public Spinner<LocalTime> startTimeSpinner;

    /** Time Spinner allows for the selection of the end time of the appointment */
    @FXML public Spinner<LocalTime> endTimeSpinner;

    /** Label displays the businesses starting hours of operations converted to the USERS time zone */
    @FXML public Label startLabel;

    /** Label displays the businesses ending hours of operations converted to the USERS time zone */
    @FXML public Label endLabel;

    /** Button which when clicked will attempt to save the appointment information into the database */
    @FXML public Button saveAppBtn;

    /** Button which when clicked will change the scene and return the user to the main 'Appointments' menu */
    @FXML public Button cancelBtn;

    /** Stores the businesses START time in UTC to be used in comparison */
    private final LocalTime utcStart = LocalTime.of(12,0);

    /** Stores the businesses END time in UTC to be used in comparison */
    private final LocalTime utcEnd = LocalTime.of(2,0);


    /** Initializes the NewAppointmentController class. A 'Customer' List and 'Contact' list are
        populated by querying the database. The names of contacts and customers
        are stored in separate lists as strings to allow for their use in the 'Customer' and 'Contact' combo boxes. Additionally,
        The date pickers are set to the current date, and the time spinners are set to the start / end times of the business
        but are converted to the users timezone in a 24-hr format.
        @param url .
        @param resourceBundle .
        RUNTIME ERROR - A SQLException is addressed through try/catch block in the event a SQL syntax error is encountered.
        LAMBDA EXPRESSIONS - 2 separate lambda expressions operate in a way similar to others in the application (found in add / update customer
        controllers, etc.). They serve as a efficient way to write cleaner code to execute a loop by iterating over a a list of customer and contact objects
        extracting name information and using those names to populate combo boxes for the user.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        appTypeCombo.setItems(appointmentTypes);

        setDatesTimes();

        ObservableList<String> contactNames = FXCollections.observableArrayList();
        ObservableList<Contacts> contacts = null;
        ObservableList<String> customerNames = FXCollections.observableArrayList();
        ObservableList<Customer> customers = null;
        ObservableList<String> userNames = FXCollections.observableArrayList();
        ObservableList<User> users = null;
        try {
            contacts = ContactsDAO.getContactName();
            customers = CustomerDAO.getCustomerName();
            users = getUserName();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //Lambda
        contacts.forEach((contact)-> {
            String addContact = contact.getContactName();
            contactNames.add(addContact);

        });
        //Lambda
        customers.forEach((customer)-> {
            String addCustomer = customer.getCustomerName();
            customerNames.add(addCustomer);
        });

        //Lambda
        users.forEach((user)-> {
            String addUser = user.getUserName();
            userNames.add(addUser);
        });
            contactNameCombo.setItems(contactNames);
            customerNameCombo.setItems(customerNames);
            userCombo.setItems(userNames);

    }

    /** This method when called attempts to save the information entered into the fields by the user and generate an appointment
        storing it in the database. The first step involves checking the dates and times the user selected to ensure that they fall within
        business hours. Then the database is searched to find if any existing appointments exist that conflict or overlap with the appointment.
        If no conflicts exist, the information entered by the user is entered into a SQL statement and the appointment is added to the database.
        The scene is then changed and the user is returned to the main appointment screen.
        @param actionEvent Triggered by clicking on the 'Save Appointment' button
        @throws SQLException The fields which require user input are used in SQL queries, thus appropriate checks are made to ensure
        that the user cannot leave fields empty. If empty fields exist the user is prompted to correct the error.
        @throws ParseException Data fields are used in methods to search for specific criteria in the database. Thus only entries which match a result
        field are allowed to be entered.
        RUNTIME ERROR - The change of scene can result in an IOException and is handled through a try/catch block which prints a more user friendly message if encountered.*/
    public void saveAppointment(ActionEvent actionEvent) throws SQLException, ParseException {
        LocalDateTime start = ldtMaker(startDatePick.getValue(), startTimeSpinner.getValue());
        LocalDateTime end = ldtMaker(endDatePick.getValue(), endTimeSpinner.getValue());
        try{
            if(titleText.getText().isEmpty() || descripText.getText().isEmpty() || locationText.getText().isEmpty() ||
            appTypeCombo.getSelectionModel().isEmpty() || customerNameCombo.getSelectionModel().isEmpty() || contactNameCombo.getSelectionModel().isEmpty()
             || userCombo.getSelectionModel().isEmpty()){
                emptyInput("Appointment");
            }
            else{
                if(dateCheck(start, end)){
                    if(!AppointmentsDAO.overlappedTime(start, end)){
                        AppointmentsDAO.addAppointment(
                                titleText.getText(),
                                descripText.getText(),
                                locationText.getText(),
                                appTypeCombo.getValue().toString(),
                                utcConversion(startDatePick.getValue(), startTimeSpinner.getValue()),
                                utcConversion(endDatePick.getValue(), endTimeSpinner.getValue()),
                                getCurrentUser(),
                                getCurrentUser(),
                                getCustomerID(customerNameCombo.getValue().toString()),
                                getUserID(userCombo.getValue().toString()),
                                getContactID(contactNameCombo.getValue().toString()));

                        toAppointmentScreen(actionEvent);
                    }
                    else{
                        appointmentOverlap();
                    }
                }
            }
        }
        catch(IOException e){
            fxmlErrorMessage();
        }
    }

    /** This method when called cancels the addition of a new appointment and returns the user to the main appointment screen.
        @param actionEvent Triggered by selecting the 'Cancel" button
        RUNTIME ERROR - Try/Catch handles the problem of a missing FXML file / failure to load in changing the scene. */
    public void cancelAppSave(ActionEvent actionEvent) {
        try{
            toAppointmentScreen(actionEvent);
        }
        catch (IOException e){
            fxmlErrorMessage();
        }
    }

    /** This method when called sets the dates to the users current day and sets the start/end times to the businesses hours of operations.
        The times are converted to the users timezone */
    public void setDatesTimes(){
        startLabel.setText(displayBusinessHours(utcStart).toString());
        endLabel.setText(displayBusinessHours(utcEnd).toString());
        startDatePick.setValue(LocalDate.now());
        endDatePick.setValue(LocalDate.now());
        startTimeSpinner.setValueFactory(startSpinner);
        startSpinner.setValue(displayBusinessHours(utcStart));
        endTimeSpinner.setValueFactory(endSpinner);
        endSpinner.setValue(displayBusinessHours(utcEnd));
    }

    /** Creates a TimeSpinner object which is used in the selection of START appointment times. The increment / decrement methods within
        change the time position of the spinner by 15 minutes. */
    SpinnerValueFactory startSpinner = new SpinnerValueFactory<LocalTime>() {
        @Override
        public void decrement(int i) {
            LocalTime localTime = getValue();
            setValue(localTime.minusHours(i));
            setValue(localTime.minusMinutes(16 - i));
        }

        @Override
        public void increment(int i) {
            LocalTime localTime = getValue();
            setValue(localTime.plusHours(i));
            setValue(localTime.plusMinutes(14 + i));
        }
    };

    /** Creates a TimeSpinner object which is used in the selection of END appointment times. The increment / decrement methods within
     change the time position of the spinner by 15 minutes. */
    SpinnerValueFactory endSpinner = new SpinnerValueFactory<LocalTime>() {
        @Override
        public void decrement(int i) {
            LocalTime localTime = getValue();
            setValue(localTime.minusHours(i));
            setValue(localTime.minusMinutes(16 - i));
        }

        @Override
        public void increment(int i) {
            LocalTime localTime = getValue();
            setValue(localTime.plusHours(i));
            setValue(localTime.plusMinutes(14 + i));
        }
    };
}
