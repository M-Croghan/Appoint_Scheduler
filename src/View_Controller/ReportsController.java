package View_Controller;

import DAO.ContactsDAO;
import DAO.CustomerDAO;
import Model.Contacts;
import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ResourceBundle;

import static DAO.AppointmentsDAO.*;
import static DAO.ContactsDAO.getContactID;
import static DAO.CustomerDAO.getCustomerID;
import static Model.Appointments.appointmentTypes;
import static Model.WarningMessages.fxmlErrorMessage;
import static Model.WarningMessages.noSelection;
import static View_Controller.ScreenChange.toMainScreen;

public class ReportsController implements Initializable {

    /** Table that displays appointment information */
    @FXML public TableView appTable;

    /** Column - displays appointment ID data */
    @FXML public TableColumn appIdCol;

    /** Column - displays appointment title info */
    @FXML public TableColumn titleCol;

    /** Column - displays appointment description data */
    @FXML public TableColumn descripCol;

    /** Column - displays appointment location data */
    @FXML public TableColumn locCol;

    /** Column - displays contact ID data */
    @FXML public TableColumn contactCol;

    /** Column - displays appointment type data */
    @FXML public TableColumn typeCol;

    /** Column - displays appointment start data */
    @FXML public TableColumn startCol;

    /** Column - displays appointment end data */
    @FXML public TableColumn endCol;

    /** Column - displays customer ID data */
    @FXML public TableColumn custIdCol;

    /** Radio Button - Selected as default, facilitates searching all existing records */
    @FXML public RadioButton allAppRadio;

    /** Radio Button - Facilitates searching records in the upcoming month if selected */
    @FXML public RadioButton monthAppRadio;

    /** Radio Button - Facilitates searching records in the upcoming week if selected */
    @FXML public RadioButton weekAppRadio;

    /** Toggle Group - Containing the 'All appointments', 'Upcoming week', and 'Upcoming month'*/
    @FXML public ToggleGroup appSearchGroup;

    /** Combo Box - Populated by a list of strings which the user can select to narrow report criteria */
    @FXML public ComboBox filterCombo;

    /** Combo Box - Populated by a list, depending on the users selection of a filter. */
    @FXML public ComboBox criteriaCombo;

    /** Combo Box - Populated by a list, containing names of months. */
    @FXML public ComboBox byMonthCombo;

    /** Button used to generate a report of appointments based on the users search criteria */
    @FXML public Button generateReportBtn;

    /** Button that resets search criteria fields to default */
    @FXML public Button resetFieldBtn;

    /** Button that changes the scene and takes the user back to main screen. */
    @FXML public Button returnMainBtn;



    /** Holds a list of a Strings which are used to populate the 'filter by' combo box. */
    private final ObservableList filterTypes = FXCollections.observableArrayList("Customer", "Contact", "Appointment Type");

    /** Holds a list of a Strings which are used to populate the 'by month' combo box. */
    private final ObservableList months = FXCollections.observableArrayList( "01 - January", "02 - February", "03 - March", "04 - April", "05 - May", "06 - June",
            "07 - July", "08 - August", "09 - September", "10 - October", "11 - November", "12 - December");

    /** Holds a portion of the SQL statement used to query a database at a weekly interval. Is passed
        into a method which concatenates a string used as SQL statement and executes the corresponding query. */
    private final String sevenDayInt = " Start BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 7 DAY)";

    /** Holds a portion of the SQL statement used to query a database at a monthly interval. Is passed
     into a method which concatenates a string used as SQL statement and executes the corresponding query.*/
    private final String monthInt = " Start BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 1 MONTH)";

    /** Displays a 'Results Returned" label to inform the user */
    public Label resultsLbl;

    /** Displays the number of results returned by the user's report query */
    public Label numResultsLbl;



    /** Contact list which will store contact information from the database */
    private ObservableList<Contacts> contacts = null;

    /** A list to hold the names of Contacts. Used to populate criteria combo box when filtered by 'Contact' */
    private ObservableList<String> contactNames = FXCollections.observableArrayList();

    /** Customer list which will store customer information from the database */
    private ObservableList<Customer> customers = null;

    /** A list to hold the names of Customers. Used to populate criteria combo box when filtered by 'Customer' */
    private ObservableList<String> customerNames = FXCollections.observableArrayList();


    /** Initializes the Reports Controller Class. Populates 'Filter' combo box to narrow report generation.
        A 'Customer' List and 'Contact' list are populated by querying the database. The names of contacts and customers
        are stored in separate lists as strings to allow for their use in combo boxes.
        RUNTIME ERROR - SQLException is handled via try/catch block in the event that their is an error in the SQL query.
        LAMBDA EXPRESSION(S) - 2 separate lambda expressions help eliminate the need for writing the larger verbose for-loops.
        When initializing the controller, these expressions operate in a manner similar to others documented in the application
        where the help to iterate over a list of customer and contact objects, extra their name data and populate their corresponding lists.
        These lists then go on to set the values of combo boxes used by the user.
        @param resourceBundle
        @param url */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        byMonthCombo.setItems(months);
        filterCombo.setItems(filterTypes);

        try {
            contacts = ContactsDAO.getContactName();
            customers = CustomerDAO.getCustomerName();
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
    }

    /** This method populates the criteria combo box based on the user selection made in the filter combo box.
        Depending on the users choice, different values will populate.
        @param actionEvent is triggered by making a selection in the filter combo box.*/
    public void filterSelected(ActionEvent actionEvent) {
        if (filterCombo.getValue().equals(filterTypes.get(0))) {
            criteriaCombo.setItems(customerNames);
        }
        if (filterCombo.getValue().equals(filterTypes.get(1))) {
            criteriaCombo.setItems(contactNames);
        }
        if (filterCombo.getValue().equals(filterTypes.get(2))) {
            criteriaCombo.setItems(appointmentTypes);
        }
//        if (filterCombo.getValue() == null){
//            System.out.println("filter box problem");
//        }
    }
    /** This method queries the database based on the specific search criteria selected in the user interface. The default search
        involves searching ALL available appointments. A user can narrow their search by customer name, contact name, or type of appointment.
        Each individual selection in the search criteria will manipulate the SQL statement used for the query. Users are able to define their
        searches by all existing, the upcoming week, and the upcoming month intervals. Additionally, a message is displayed in the user interface
        listing the total number of items returned in the report.
        @param actionEvent is triggered by clicking on the 'Generate report button.
        @throws SQLException - sql exception thrown in the event that their is an error in the SQL syntax.
        @throws  ParseException - because the user interface deals with search parameters that are user friendly and not directly related to columns
                in the user interface, methods are called to parse the users select and return the appropriate search criteria for the SQL query.*/
    public void generateReport(ActionEvent actionEvent) throws SQLException, ParseException {
        // SELECTS ALL APPOINTMENTS AND ALLOWS FOR EXTENDED SEARCH OPTIONS
        if(allAppRadio.isSelected()){
            // SELECTS ALL APPOINTMENTS BY FILTER TYPE & SELECTED CRITERIA
            if(!filterCombo.getSelectionModel().isEmpty() && !criteriaCombo.getSelectionModel().isEmpty() && byMonthCombo.getSelectionModel().isEmpty()){
                if(filterCombo.getValue().equals(filterTypes.get(0))){
                    //All appointments -- Customer
                    appTable.setItems(getAppointmentsByType("Customer_ID", getCustomerID(criteriaCombo.getValue().toString()), ""));

                }
                else if(filterCombo.getValue().equals(filterTypes.get(1))){
                    //All appointments -- Contact
                    appTable.setItems(getAppointmentsByType("Contact_ID", getContactID(criteriaCombo.getValue().toString()), ""));

                }
                else if(filterCombo.getValue().equals(filterTypes.get(2))){
                    //All appointments -- Type
                    appTable.setItems(getAppointmentsByName("Type", criteriaCombo.getValue().toString(), ""));
                }
            }
            // SELECTS ALL APPOINTMENTS BY MONTH, FILTER TYPE, & SELECTED CRITERIA
            else if (!filterCombo.getSelectionModel().isEmpty() && !criteriaCombo.getSelectionModel().isEmpty() && !byMonthCombo.getSelectionModel().isEmpty()) {
                if (filterCombo.getValue().equals(filterTypes.get(0))) {
                    appTable.setItems(getAppointmentsByType("Customer_ID", getCustomerID(criteriaCombo.getValue().toString()),
                            " AND month(Start) = " + byMonthCombo.getValue().toString().substring(0,2)));
                }
                else if (filterCombo.getValue().equals(filterTypes.get(1))){
                    appTable.setItems(getAppointmentsByType("Contact_ID", getContactID(criteriaCombo.getValue().toString()),
                            " AND month(Start) = " + byMonthCombo.getValue().toString().substring(0,2)));
                }
                else if(filterCombo.getValue().equals(filterTypes.get(2))){
                    appTable.setItems(getAppointmentsByName("Type", criteriaCombo.getValue().toString(),
                            " AND month(Start) = " + byMonthCombo.getValue().toString().substring(0,2)));
                }
            }
            // SELECTS ALL APPOINTS IN GENERAL OR BY MONTH REGARDLESS OF TYPE
            else if(filterCombo.getSelectionModel().isEmpty() && criteriaCombo.getSelectionModel().isEmpty()){
                if(!byMonthCombo.getSelectionModel().isEmpty()){
                    appTable.setItems(getAppointmentsByTime(" WHERE month(Start) = " + byMonthCombo.getValue().toString().substring(0,2)));
                }
                else{
                    // All appointments -- General
                    appTable.setItems(getAppointments());
                }
            }
            else if(!filterCombo.getSelectionModel().isEmpty() && criteriaCombo.getSelectionModel().isEmpty()){
                noSelection("Criteria", "search");
            }
        }
        // SELECTS ALL APPOINTMENTS UPCOMING IN THE NEXT WEEKLY INTERVAL
        else if(weekAppRadio.isSelected()){
            if(!filterCombo.getSelectionModel().isEmpty() && !criteriaCombo.getSelectionModel().isEmpty()){
                if(filterCombo.getValue().equals(filterTypes.get(0))){
                    appTable.setItems(getAppointmentsByType("Customer_ID", getCustomerID(criteriaCombo.getValue().toString()), " AND " + sevenDayInt));
                }
                else if(filterCombo.getValue().equals(filterTypes.get(1))){
                    appTable.setItems(getAppointmentsByType("Contact_ID", getContactID(criteriaCombo.getValue().toString()), " AND " + sevenDayInt));
                }
                else if(filterCombo.getValue().equals(filterTypes.get(2))){
                    appTable.setItems(getAppointmentsByName("Type", criteriaCombo.getValue().toString(), " AND " + sevenDayInt));
                }
            }
            else if(filterCombo.getSelectionModel().isEmpty() && criteriaCombo.getSelectionModel().isEmpty()){
                appTable.setItems(getAppointmentsByTime("WHERE Start BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 7 DAY)"));
            }
            else if(!filterCombo.getSelectionModel().isEmpty() && criteriaCombo.getSelectionModel().isEmpty()){
                noSelection("Criteria", "search");
            }
        }
        // SELECTS ALL APPOINTMENTS UPCOMING IN THE NEXT MONTHLY INTERVAL
        else if(monthAppRadio.isSelected()){
                if(!filterCombo.getSelectionModel().isEmpty() && !criteriaCombo.getSelectionModel().isEmpty()){
                    if(filterCombo.getValue().equals(filterTypes.get(0))){
                        appTable.setItems(getAppointmentsByType("Customer_ID", getCustomerID(criteriaCombo.getValue().toString()), " AND " + monthInt));
                    }
                    else if(filterCombo.getValue().equals(filterTypes.get(1))){
                        appTable.setItems(getAppointmentsByType("Contact_ID", getContactID(criteriaCombo.getValue().toString()), " AND " + monthInt));
                    }
                    else if(filterCombo.getValue().equals(filterTypes.get(2))){
                        appTable.setItems(getAppointmentsByName("Type", criteriaCombo.getValue().toString(), " AND " + monthInt));
                    }
                }
                else if(filterCombo.getSelectionModel().isEmpty() && criteriaCombo.getSelectionModel().isEmpty()){
                    appTable.setItems(getAppointmentsByTime("WHERE Start BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 1 MONTH)"));
                }
                else if(!filterCombo.getSelectionModel().isEmpty() && criteriaCombo.getSelectionModel().isEmpty()){
                    noSelection("Criteria", "search");
                }
            }
        // Sets the table columns based on what information is retrieved as a result of search selection criteria.
        appIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descripCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>(("startTime")));
        endCol.setCellValueFactory(new PropertyValueFactory<>(("endTime")));
        custIdCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));

        numResultsLbl.setText(String.valueOf(appTable.getItems().size()));


}

    /** This method directs the user to the main menu. Selecting the 'Main Menu' button changes the scene and loads the FXML file of the main screen.
     @param actionEvent Triggered by the 'Main Menu' button: sets the screen title, scene, and displays the window.
     RUNTIME ERROR results in an IOException in the event it cannot locate the FXML source file. Upon catching the exception an error
     message is printed to the console.*/
    public void returnToMain(ActionEvent actionEvent){
        try{
            toMainScreen(actionEvent);
        }
        catch(IOException e){
            fxmlErrorMessage();
        }
    }

    /** This method resets all of the search parameters in the user interface. The default radio button is set to 'All Appointments'
        and the combo boxes are reset / not selected. The report tablle and the label listing the total number of appointments
        returned in the report are also cleared.
        @param actionEvent - triggered by clicking the 'Reset Fields' button. */
    public void resetFields(ActionEvent actionEvent) {
        allAppRadio.setSelected(true);
        criteriaCombo.setItems(null);
        filterCombo.setValue("");
        byMonthCombo.setValue("");
        numResultsLbl.setText("");
        appTable.getItems().clear();
        enableByMonth(actionEvent);
    }

    public void disableByMonth(ActionEvent actionEvent) {
        byMonthCombo.setDisable(true);
    }

    public void enableByMonth(ActionEvent actionEvent) {
        byMonthCombo.setDisable(false);
    }
}
