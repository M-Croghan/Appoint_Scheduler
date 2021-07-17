package View_Controller;

import DAO.AppointmentsDAO;
import DAO.ContactsDAO;
import DAO.UserDAO;
import Model.Appointments;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

import static DAO.AppointmentsDAO.overlappedTime;
import static DAO.ContactsDAO.getContactID;
import static DAO.CustomerDAO.getCustomerID;
import static DAO.CustomerDAO.getCustomerName;
import static DAO.UserDAO.getUserID;
import static DAO.UserDAO.getUserName;
import static Model.Appointments.*;
import static Model.WarningMessages.*;
import static View_Controller.ScreenChange.toAppointmentScreen;
import static View_Controller.AppointmentController.getSelectedAppointment;
import static View_Controller.LoginController.getCurrentUser;
import static View_Controller.LoginController.getCurrentUserID;

/** Controller class for editing existing appointments */
public class EditAppointmentController implements Initializable {

    /**
     * Text field which displays appointment ID. This field is disabled / uneditable.
     */
    @FXML
    public TextField appIDtext;

    /**
     * Text field which displays the selected appointment title and allows editing
     */
    @FXML
    public TextField titleText;

    /**
     * Text field which displays the selected appointment location and allows editing
     */
    @FXML
    public TextField locationText;

    /**
     * Text field which displays the selected appointment description and allows editing
     */
    @FXML
    public TextArea descripText;

    /** Combo box which hold customer names*/
    @FXML
    public ComboBox customerNameCombo;

    /**
     * Combo box which displays the selected appointment contact name and allows editing
     */
    @FXML
    public ComboBox contactNameCombo;

    /**
     * Combo box which displays the selected appointment type name and allows editing
     */
    @FXML
    public ComboBox appTypeCombo;

    /**
     * Date picker displays the selected appointment start date and allows editing
     */
    @FXML
    public DatePicker startDatePick;

    /**
     * Date picker displays the selected appointment end date and allows editing
     */
    @FXML
    public DatePicker endDatePick;

    /**
     * Time Spinner displays the selected appointment start time and allows editing
     */
    @FXML
    public Spinner<LocalTime> startTimeSpinner;

    /**
     * Time Spinner displays the selected appointment end time and allows editing
     */
    @FXML
    public Spinner<LocalTime> endTimeSpinner;

    /**
     * Button which when clicked will attempt to update the existing appointment information into the database
     */
    @FXML
    public Button saveAppBtn;

    /**
     * Button which when clicked will change the scene and return the user to the main 'Appointments' menu
     */
    @FXML
    public Button cancelBtn;

    /**
     * Label displays the businesses starting time converted to the USERS time zone
     */
    @FXML
    public Label startLabel;

    /** Combo box which lists User to associate with an appointment */
    public ComboBox userCombo;

    /**
     * Label displays the businesses closing time converted to the USERS time zone
     */
    @FXML
    public Label endLabel;

    /**
     * Stores the businesses START time in UTC to be used in comparison
     */
    private final LocalTime utcStart = LocalTime.of(12, 0);

    /**
     * Stores the businesses END time in UTC to be used in comparison
     */
    private final LocalTime utcEnd = LocalTime.of(2, 0);

    /**
     * Retrieves and stores the selected appointment and enables the population of appointment fields with information specific to that appointment.
     */
    public final Appointments selectedAppointment = getSelectedAppointment();



    /**
     * Initializes the EditAppointmentController class. initialize handles populating all of the relevant and editable appointment
     * information fields based on the specific appointment selected from the main appointment screen. A 'Customer' List and 'Contact'
     * list are populated by querying the database. The names of contacts and customers are stored in separate lists as strings
     * to allow for their use in the 'Customer' and 'Contact' combo boxes. Additionally, The date pickers are set to the current date,
     * and the time spinners are set to the start / end times of the business but are converted to the users timezone in a 24-hr format.
     *
     * @param url            .
     * @param resourceBundle .
     *                       RUNTIME ERROR - A SQLException is addressed through try/catch block in the event a SQL syntax error is encountered.
     *                       LAMBDA EXPRESSIONS - 2 separate lambda expressions operate in a way similar to others in the application (found in reports, add / update customer
     *                       controllers, etc.). They serve as a efficient way to write cleaner code to execute a loop by iterating over a a list of customer and contact objects
     *                       extracting name information and using those names to populate combo boxes for the user.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        appIDtext.setText(String.valueOf(selectedAppointment.getAppointmentID()));
        titleText.setText(selectedAppointment.getTitle());
        appTypeCombo.setValue(selectedAppointment.getType());
        locationText.setText(selectedAppointment.getLocation());
        descripText.setText(selectedAppointment.getDescription());
        startDatePick.setValue(dateSplitter(selectedAppointment.getStartTime()));
        endDatePick.setValue(dateSplitter(selectedAppointment.getEndTime()));
        setDatesTimes();

        try {
            customerNameCombo.setValue(getCustomerName(selectedAppointment.getCustomerID()));
            contactNameCombo.setValue(ContactsDAO.getContactName(selectedAppointment.getContactID()));
            userCombo.setValue(getUserName(selectedAppointment.getUserID()));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        ObservableList<String> contactNames = FXCollections.observableArrayList();
        ObservableList<Contacts> contacts = null;
        ObservableList<String> customerNames = FXCollections.observableArrayList();
        ObservableList<Customer> customers = null;
        ObservableList<String> userNames = FXCollections.observableArrayList();
        ObservableList<User> users = null;
        try {
            contacts = ContactsDAO.getContactName();
            customers = getCustomerName();
            users = getUserName();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //Lambda
        contacts.forEach((contact) -> {
            String addContact = contact.getContactName();
            contactNames.add(addContact);

        });
        //Lambda
        customers.forEach((customer) -> {
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
        appTypeCombo.setItems(appointmentTypes);
        userCombo.setItems(userNames);

    }

    /**
     * This method when called attempts to update an appointment by using information entered into the fields by the user.
     * The first step involves checking the dates and times the user selected to ensure that they fall within business hours.
     * Then the database is searched to find if any existing appointments exist that conflict or overlap with the appointment.
     * If no conflicts exist, the information entered by the user is entered into a SQL statement and the appointment is added to the database.
     * An additional check is performed as the appointment itself could trigger a time conflict.
     * The scene is then changed and the user is returned to the main appointment screen.
     *
     * @param actionEvent Triggered by clicking on the 'Edit Appointment' button
     * @throws SQLException   The fields which require user input are used in SQL queries, thus appropriate checks are made to ensure
     *                        that the user cannot leave fields empty. If empty fields exist the user is prompted to correct the error.
     * @throws IOException thrown if the FXML file can not load
     *                        RUNTIME ERROR - The change of scene can result in an IOException and is handled through a try/catch block which prints a more user friendly message if encoutnered.
     */
    public void editAppointment(ActionEvent actionEvent) throws SQLException, IOException {

        LocalDateTime start = ldtMaker(startDatePick.getValue(), startTimeSpinner.getValue());
        LocalDateTime end = ldtMaker(endDatePick.getValue(), endTimeSpinner.getValue());

        if (titleText.getText().isEmpty() || descripText.getText().isEmpty() || locationText.getText().isEmpty() ||
                appTypeCombo.getSelectionModel().isEmpty() || customerNameCombo.getSelectionModel().isEmpty() || contactNameCombo.getSelectionModel().isEmpty()) {
            emptyInput("Appointment");
        }
        else {
            if (dateCheck(start, end)) {
                if(!overlappedTime(start, end)){
                    AppointmentsDAO.editAppointment(
                            Integer.parseInt(appIDtext.getText()),
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


    /** This method when called cancels the update of an existing appointment and returns the user to the main appointment screen.
     @param actionEvent Triggered by selecting the 'Cancel" button
     RUNTIME ERROR - Try/Catch handles the problem of a missing FXML file / failure to load in changing the scene. */
    public void cancelAppEdit(ActionEvent actionEvent) throws IOException {
        toAppointmentScreen(actionEvent);
    }

    /** This method when called sets the selected appointments start/end dates and times. */
    public void setDatesTimes(){
        startLabel.setText(displayBusinessHours(utcStart).toString());
        endLabel.setText(displayBusinessHours(utcEnd).toString());
        startTimeSpinner.setValueFactory(startSpinner);
        startSpinner.setValue(displayUserTime(timeSplitter(selectedAppointment.getStartTime())));
        endTimeSpinner.setValueFactory(endSpinner);
        endSpinner.setValue(displayUserTime(timeSplitter(selectedAppointment.getEndTime())));
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
            setValue(localTime.plusMinutes(i + 14));
        }
    };



}
