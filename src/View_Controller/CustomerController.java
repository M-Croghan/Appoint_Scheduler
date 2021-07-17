package View_Controller;

import DAO.CustomerDAO;
import Model.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static Model.WarningMessages.fxmlErrorMessage;
import static Model.WarningMessages.noSelection;
import static View_Controller.ScreenChange.*;

/** Controller class for the Main Customer Screen */
public class CustomerController implements Initializable {
    /** Button that moves to the add new customer screen */
    @FXML public Button addNewCustBtn;

    /** Button that moves to edit customer screen */
    @FXML public Button toEditCustBtn;

    /** Button that returns to the main menu */
    @FXML public Button returnMainBtn;

    /** Button that attempts to delete the selected customer */
    @FXML public Button deleteCustBtn;

    /** Table view that displays customer records  */
    @FXML public TableView custTable;

    /** Column that displays customer's division data */
    @FXML public TableColumn custDivCol;

    /** Column that displays customer's phone data */
    @FXML public TableColumn custPhoneCol;

    /** Column that displays customer's postal code data */
    @FXML public TableColumn custPostalCol;

    /** Column that displays customer's address data */
    @FXML public TableColumn custAddressCol;

    /** Column that displays customer's name data */
    @FXML public TableColumn custNameCol;

    /** Column that displays customer's ID data */
    @FXML public TableColumn custIdCol;

    /** Customer objected used to store the selected customer in the table view to use for editing customer information. */
    private static Customer selectedCustomer;


    /** Initializes the main customer screen. Queries the database and populates the table view with all available customers.
        @param url .
        @param resourceBundle .
        RUNTIME ERROR SQLException is addressed by try/catch in the event that an error in SQL syntax is encountered when
        searching the Customer table in the database. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            custTable.setItems(CustomerDAO.getCustomer());
            custIdCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            custNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            custAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
            custPostalCol.setCellValueFactory(new PropertyValueFactory<>("zip"));
            custPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
            custDivCol.setCellValueFactory(new PropertyValueFactory<>("divID"));

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /** This method when called changes the scene and takes the user to the 'new customer screen'.
        @param actionEvent Triggered by selecting the 'New Customer' button.
        RUNTIME ERROR - IOException occurs if FXML file fails to load / isn't found. */
    public void toNewCustomer(ActionEvent actionEvent) {
        try{
            toAddCustomerScreen(actionEvent);
        }
        catch(IOException e){
            fxmlErrorMessage();
        }
    }

    /** This method when called changes the scene and takes the user to the 'edit customer screen'.
        If the user fails to select a customer, they are prompted to do so.
     @param actionEvent Triggered by selecting the 'Edit Customer' button.
     RUNTIME ERROR - IOException occurs if FXML file fails to load / isn't found.*/
    public void toEditCustomer(ActionEvent actionEvent) {
        selectedCustomer = (Customer) custTable.getSelectionModel().getSelectedItem();
        try{
            if(selectedCustomer != null){
                if(actionEvent.getSource() == toEditCustBtn){
                    toEditCustomerScreen(actionEvent);
                }
            }
            else{
                Alert noSelection = new Alert(Alert.AlertType.INFORMATION);
                noSelection.setTitle("No Customer Selected!");
                noSelection.setHeaderText("Please select a customer to update.");
                noSelection.showAndWait();
            }
        }
        catch(IOException e){
            fxmlErrorMessage();
        }

    }

    /** This method when called changes the scene and takes the user to the 'main menu'.
     @param actionEvent Triggered by selecting the 'Cancel' button.
     RUNTIME ERROR - IOException occurs if FXML file fails to load / isn't found.*/
    public void returnToMain(ActionEvent actionEvent) {
        try{
            toMainScreen(actionEvent);
        }
        catch(IOException e){
            fxmlErrorMessage();
        }
    }

    /** This method attempts to delete a customer from the customer table in the database.
        Logic check involves that the user actually select a customer or they are prompted to do so. The user
        is also prompted to confirm their deletion. If confirmed, a deletion SQL statement is executed removing the customer
        from the database and an alert is generated either confirming customer deletion or failure to do so.
        @param actionEvent Triggered by selecting the 'Delete Customer' button.
        RUNTIME ERRORS - NullPointerException and SQLException are handled through try / catch block which attempts to
        delete the customer from the database. Prompts ensure that the customer 1) exists, 2) is not associated with
        existing appointments thus preserving Referential integrity */
    public void deleteCustomer(ActionEvent actionEvent) {
        Customer customer = (Customer) custTable.getSelectionModel().getSelectedItem();
        if (customer == null){
            noSelection("Customer", "delete");
        }
        else {
            Alert deleteWarning = new Alert(Alert.AlertType.CONFIRMATION);
            deleteWarning.setTitle("Delete Customer?");
            deleteWarning.setHeaderText("Are you sure you want to delete the selected customer?");
            deleteWarning.setContentText("Click OK to delete.");
            deleteWarning.showAndWait();
            if (deleteWarning.getResult() == ButtonType.OK) {
                try {
                    int custID = customer.getCustomerID();
                    CustomerDAO.deleteCustomer(custID);
                    custTable.getItems().clear();
                    custTable.setItems(CustomerDAO.getCustomer());
                    Alert deleted = new Alert(Alert.AlertType.CONFIRMATION);
                    deleted.setTitle("Customer Deleted!");
                    deleted.setHeaderText("Customer ID: " + customer.getCustomerID() + " / Name: " + customer.getCustomerName());
                    deleted.setContentText("This customer has been deleted.");
                    deleted.showAndWait();
                } catch (NullPointerException | SQLException e) {
                    Alert deleted = new Alert(Alert.AlertType.ERROR);
                    deleted.setTitle("Unable to delete customer!");
                    deleted.setHeaderText("Customer ID: " + customer.getCustomerID() + " / Name: " + customer.getCustomerName());
                    deleted.setContentText("This customer has scheduled appointments. Appointments must be cancelled before customer deletion.");
                    deleted.showAndWait();
                }
            }
        }
    }
    /** This method when called returns the selected customer in the table view.
        @return customer object selected from the Customer table. */
    public static Customer getSelectedCustomer(){
        return selectedCustomer;
    }
}
