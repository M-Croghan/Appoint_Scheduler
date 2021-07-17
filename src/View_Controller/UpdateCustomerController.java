package View_Controller;

import DAO.CountriesDAO;
import DAO.CustomerDAO;
import DAO.DivisionDAO;
import Model.Countries;
import Model.Customer;
import Model.Division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

import java.util.ResourceBundle;

import static Model.WarningMessages.*;
import static View_Controller.CustomerController.getSelectedCustomer;
import static View_Controller.LoginController.getCurrentUser;
import static View_Controller.ScreenChange.toCustomerScreen;

/**  Controller class for updating existing customer information */
public class UpdateCustomerController implements Initializable {

    /** Text field that displays customer ID. This field is disabled and uneditable */
    @FXML public TextField custIdText;

    /** Text field that displays the selected customer name and collects information if edited. */
    @FXML public TextField custNameText;

    /** Text field that displays customer address and collects information if edited. */
    @FXML public TextField custAddressText;

    /** Text field that displays customer postal code and collects information if edited. */
    @FXML public TextField custPostalText;

    /** Text field that displays customer phone number and collects information if edited. */
    @FXML public TextField custPhoneText;

    /** Combo box which displays the selected customer's country info and allows for it to be changed / edited. */
    @FXML public ComboBox countryCombo;

    /** Combo box which displays the selected customer's state or province info and allows for it to be changed / edited. */
    @FXML public ComboBox stateProvCombo;

    /** Button that cancels the edit / update action to the selected customer and returns to the main customer screen */
    @FXML public Button cancelBtn;

    /** Button that saves the updated customer information, stores it in the database and then returns to the main customer screen */
    @FXML public Button saveCustomerBtn;


    /** Retrieves and stored the selected customer and enables the population of customer fields with information specific to that customer. */
    public final Customer selectedCustomer = getSelectedCustomer();


    /** Initializes the UpdateCustomerController Class. initialize handles populating all of the relevant and editable customer
        customer information fields based on the specific customer selected from the main customer screen. The values for 1st
        level division data (country and state / province) are set similar to the other specific customer info, however each
        combo box must be populated with the corresponding info from the database. Thus each combo is surrounded by try/catch
        block to handle the potential SQLException in retrieving that information.
        @param url
        @param  resourceBundle
        LAMBDA EXPRESSION(S) - In an effort to minimize huge blocks of code in an already complex method, several lambda expressions iterate over a list of division object info
        and adds to a list of strings which is used to populate the combo boxes. Within this method, 5 separate lambdas operate in the same way  for each country and division.
        This implementation helps in not having to write larger for loops for each country and division list.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Populates the country combo box.
        try {
            ObservableList<String> countryNames = FXCollections.observableArrayList();
            ObservableList<Countries> countries = CountriesDAO.getCountries();

            //Lambda
            countries.forEach((country)-> {
                String addCountry = country.getCountryName();
                countryNames.add(addCountry);

            });

            countryCombo.setItems(countryNames);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        // Sets the customer information fields based on the selected customer in the selected customer field.
        custIdText.setText(String.valueOf(selectedCustomer.getCustomerID()));
        custNameText.setText(selectedCustomer.getCustomerName());
        custAddressText.setText(selectedCustomer.getAddress());
        custPostalText.setText(selectedCustomer.getZip());
        custPhoneText.setText(selectedCustomer.getPhone());

        // Initializes / populates the country & state/province combo boxes based on the customer information stored within the database.
        try {
            stateProvCombo.setValue(DivisionDAO.getDivName(selectedCustomer.getDivID()));
            countryCombo.setValue(DivisionDAO.getCountryName(selectedCustomer.getDivID()));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        // Initializes / populates the the country & state/province combo boxes with 1st-level division data depending on the selection / information
        // of the selected customer.
        try {
            ObservableList<String> countryNames = FXCollections.observableArrayList();
            ObservableList<Countries> countries = CountriesDAO.getCountries();
            //Lambda
            countries.forEach((country)-> {
                String addCountry = country.getCountryName();
                countryNames.add(addCountry);

            });

            countryCombo.setItems(countryNames);

            if(countryCombo.getValue().equals("Canada")){
                try {
                    ObservableList<String> canadaProv = FXCollections.observableArrayList();
                    ObservableList<Division> canProvinces = DivisionDAO.getDivision(38);


                    //Lambda
                    canProvinces.forEach((province)-> {
                        String addProvince = province.getDivisionName();
                        canadaProv.add(addProvince);

                    });

                    stateProvCombo.setItems(canadaProv);

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            else if(countryCombo.getValue().equals("United Kingdom")){
                try {
                    ObservableList<String> ukProv = FXCollections.observableArrayList();
                    ObservableList<Division> ukProvinces = DivisionDAO.getDivision(230);

                    //Lambda
                    ukProvinces.forEach((province)-> {
                        String addUKProvince = province.getDivisionName();
                        ukProv.add(addUKProvince);

                    });

                    stateProvCombo.setItems(ukProv);

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            else if(countryCombo.getValue().equals("United States")){
                try {
                    ObservableList<String> usState = FXCollections.observableArrayList();
                    ObservableList<Division> states = DivisionDAO.getDivision(231);

                    //Lambda
                    states.forEach((state)-> {
                        String addState = state.getDivisionName();
                        usState.add(addState);

                    });

                    stateProvCombo.setItems(usState);

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
    /** This method updates the 'State / Division' combo box with the appropriate information based on the country selected from the 'Country' combo box.
        Three main countries can be selected (Canada, United Kingdom, United States). When a country is chosen, the database is queried for 1st-level division
        information related to that country and populates the state / province combo box with related names.
        @param actionEvent - user clicking / selecting a specific country.
        RUNTIME ERROR - Each selection queries the database and thus has potential to throw a SQLException. A try/catch block is implemented for each country selection.
        LAMBDA EXPRESSIONS - The lambdas within this method operate similar to those found in the controllers initialization. They help to minimize code needed to
        loop through a list of objects and add to a list to populate combo boxes interacted with by the user. 3 such expressions exist within this method.*/
    public void updateStateCombo(ActionEvent actionEvent) {
        if(countryCombo.getValue().equals("Canada")){
            try {
                ObservableList<String> canadaProv = FXCollections.observableArrayList();
                ObservableList<Division> canProvinces = DivisionDAO.getDivision(38);

                //Lambda
                canProvinces.forEach((province)-> {
                    String addProvince = province.getDivisionName();
                    canadaProv.add(addProvince);

                });

                stateProvCombo.setItems(canadaProv);

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        else if(countryCombo.getValue().equals("United Kingdom")){
            try {
                ObservableList<String> ukProv = FXCollections.observableArrayList();
                ObservableList<Division> ukProvinces = DivisionDAO.getDivision(230);

                //Lambda
                ukProvinces.forEach((province)-> {
                    String addUKProvince = province.getDivisionName();
                    ukProv.add(addUKProvince);

                });

                stateProvCombo.setItems(ukProv);

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        else if(countryCombo.getValue().equals("United States")){
            try {
                ObservableList<String> usState = FXCollections.observableArrayList();
                ObservableList<Division> states = DivisionDAO.getDivision(231);

                //Lambda
                states.forEach((state)-> {
                    String addState = state.getDivisionName();
                    usState.add(addState);

                });

                stateProvCombo.setItems(usState);

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    /** This method takes the information entered in the editable fields of the user interface and updates the stored customer in the database.
         @param actionEvent Triggered by the 'Save' button: passes the text field and combo box information to the updateCustomer method which executes specific SQL statement
         to save the updated customer. Additionally, it call a scene change method which sets the screen title, scene, and displays main customer window.
         RUNTIME ERROR(S) - IOException and SQLException handled through try/catch and error messages are printed to the console.
         LOGIC CHECK - Ensures all customer fields must be filled out. If any are discovered empty, the user is prompted to correct the missing fields. */
    public void saveCustomer(ActionEvent actionEvent) {
        if(custNameText.getText().isEmpty() || custAddressText.getText().isEmpty() || custPhoneText.getText().isEmpty() || custPostalText.getText().isEmpty()
        || countryCombo.getSelectionModel().isEmpty() || stateProvCombo.getSelectionModel().isEmpty()){
            emptyInput("Customer");
        }
        else{
            try {
                CustomerDAO.updateCustomer(Integer.parseInt(custIdText.getText()), custNameText.getText(), custAddressText.getText(),
                        custPostalText.getText(), custPhoneText.getText(), Timestamp.from(Instant.now()),
                        getCurrentUser(), DivisionDAO.getDivisionID((String) stateProvCombo.getValue()));

                toCustomerScreen(actionEvent);
            }
            catch (IOException e){
                fxmlErrorMessage();
            }
            catch (SQLException e){
                sqlErrorMessge();
            }
        }
    }

    /** This method directs the user to the Customers menu. Selecting the 'Cancel' button changes the scene and loads the FXML file of the main screen.
     @param actionEvent Triggered by the 'Cancel' button: sets the screen title, scene, and displays the window.
     RUNTIME ERROR results in an IOException in the event it cannot locate the FXML source file. Upon catching the exception an error
     message is printed to the console.*/
    public void returnToCustomer(ActionEvent actionEvent){
        try{
            toCustomerScreen(actionEvent);
        }
        catch (IOException e){
            fxmlErrorMessage();
        }
    }
}
