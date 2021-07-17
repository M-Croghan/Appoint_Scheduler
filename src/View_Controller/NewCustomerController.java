package View_Controller;

import DAO.CountriesDAO;
import DAO.CustomerDAO;
import DAO.DivisionDAO;
import Model.Countries;
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
import static View_Controller.LoginController.getCurrentUser;
import static View_Controller.ScreenChange.toCustomerScreen;

/**  Controller class for creating a new customer */
public class NewCustomerController implements Initializable {

    /** Text field that collects customer's name information. */
    @FXML public TextField custNameText;

    /** Text field that collects customer's address information. */
    @FXML public TextField custAddressText;

    /** Text field that collects customer's phone information. */
    @FXML public TextField custPhoneText;

    /** Text field that collects customer's postal code information. */
    @FXML public TextField custPostalText;

    /** Button that cancels the creation of a new customer and returns to the main customer screen */
    @FXML public Button cancelBtn;

    /** Button that saves the customer information, stores it in the database and then returns to the main customer screen */
    @FXML public Button saveCustomerBtn;

    /** Combo box which displays country info and allows for it to be changed / edited. */
    @FXML public ComboBox countryCombo;

    /** Combo box which displays state or province info based on country selected and allows for it to be changed / edited. */
    @FXML public ComboBox stateProvCombo;



    /** Initializes the NewCustomerController Class. initialize handles populating the values for 1st
     level division data (country). The database is queried to set the combo box to a countries name.
     @param url
     @param  resourceBundle
     LAMBDA EXPRESSION - A lambda expression is used in place of for-loop for cleaner / more concise code
     and helps to iterate over a list of country objects, extract their name information and populate a list of strings
     that help set the value of the country combo box.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            ObservableList<String> countryNames = FXCollections.observableArrayList();
            ObservableList<Countries> countries = CountriesDAO.getCountries();

            countries.forEach((country)-> {
                String addCountry = country.getCountryName();
                countryNames.add(addCountry);

            });

            countryCombo.setItems(countryNames);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /** This method updates the 'State / Division' combo box with the appropriate information based on the country selected from the 'Country' combo box.
     Three main countries can be selected (Canada, United Kingdom, United States). When a country is chosen, the database is queried for 1st-level division
     information related to that country and populates the state / province combo box with related names.
     @param actionEvent - user clicking / selecting a specific country.
     RUNTIME ERROR - Each selection queries the database and thus has potential to throw a SQLException. A try/catch block is implemented for each country selection.
     LAMBDA EXPRESSION(S) - 3 separate but similar lambda expressions help to iterate over lists of objects and populate string lists that help to set values in combo boxes*/
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

    /** This method takes the information entered in the editable fields of the user interface and stores a new customer in the database.
     @param actionEvent Triggered by the 'Save' button: passes the text field and combo box information to the addCustomer method which executes specific SQL statement
     to save the new customer. Additionally, it calls a scene change method which sets the screen title, scene, and displays main customer window.
     RUNTIME ERROR(S) - IOException and SQLException handled through try/catch and error messages are printed to the console.
     LOGIC CHECK - Ensures all customer fields must be filled out. If any are discovered empty, the user is prompted to correct the missing fields. */
    public void saveCustomer(ActionEvent actionEvent){
        if(custNameText.getText().isEmpty() || custAddressText.getText().isEmpty() || custPhoneText.getText().isEmpty() || custPostalText.getText().isEmpty()
                || countryCombo.getSelectionModel().isEmpty() || stateProvCombo.getSelectionModel().isEmpty()){
            emptyInput("Customer");
        }
        else{
            try{
                CustomerDAO.addCustomer(custNameText.getText(), custAddressText.getText(), custPostalText.getText(), custPhoneText.getText(),
                        getCurrentUser(), Timestamp.from(Instant.now()), getCurrentUser(), DivisionDAO.getDivisionID((String) stateProvCombo.getValue()));

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

    /** This method directs the user to the Customers menu. Selecting the 'Cancel' button the 'toCustomerScreen' method is called
        which changes the scene and loads the FXML file of the main screen.
        @param actionEvent Triggered by the 'Cancel' button: sets the screen title, scene, and displays the window.
        RUNTIME ERROR results in an IOException in the event it cannot locate the FXML source file. Upon catching the exception an error
        message is printed to the console. */
    public void returnToCustomer(ActionEvent actionEvent) {
        try{
            toCustomerScreen(actionEvent);
        }
        catch (IOException e){
            fxmlErrorMessage();
        }
    }
}
