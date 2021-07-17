package View_Controller;

import DAO.AppointmentsDAO;
import Model.Appointments;
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

import static Model.WarningMessages.fxmlErrorMessage;
import static View_Controller.ScreenChange.*;

/** Controller class for main appointments screen */
public class AppointmentController implements Initializable {


    /** Table view that displays appointment records  */
    @FXML public TableView appTable;

    /** Column that displays appointment's ID data */
    @FXML public TableColumn appIdCol;

    /** Column that displays appointment's title data */
    @FXML public TableColumn titleCol;

    /** Column that displays appointment's description data */
    @FXML public TableColumn descripCol;

    /** Column that displays appointment's location data */
    @FXML public TableColumn locCol;

    /** Column that displays appointment's type data */
    @FXML public TableColumn typeCol;

    /** Column that displays appointment's contact ID data */
    @FXML public TableColumn contactCol;

    /** Column that displays appointment's start time data */
    @FXML public TableColumn startCol;

    /** Column that displays appointment's end time data */
    @FXML public TableColumn endCol;

    /** Column that displays appointment's customer ID data */
    @FXML public TableColumn custIdCol;

    /** Column that displays appointment's user ID data */
    @FXML public TableColumn userCol;

    /** Button that moves to the add new appointment screen */
    @FXML public Button addNewAppBtn;

    /** Button that moves to edit appointment screen */
    @FXML public Button toEditAppBtn;

    /** Button that returns to the main menu */
    @FXML public Button returnMainBtn;

    /** Button that attempts to delete the selected appointment */
    @FXML public Button deleteAppBtn;



    /** Appointments object used to store the selected appointment in the table view to use for editing appointment information. */
    public static Appointments selectedAppointment;



    /** Initializes the main appointment screen. Queries the database and populates the table view with all available appointments.
     @param url .
     @param resourceBundle .
     RUNTIME ERROR SQLException/ParseException is addressed by try/catch in the event that an error in SQL syntax is encountered when
     searching the Appointments table in the database or by parsing through data in an attempt to successfully execute an accurate SQL statement. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            appTable.setItems(AppointmentsDAO.getAppointments());
            appIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
            titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
            descripCol.setCellValueFactory(new PropertyValueFactory<>("description"));
            locCol.setCellValueFactory(new PropertyValueFactory<>("location"));
            contactCol.setCellValueFactory(new PropertyValueFactory<>("contactID"));
            typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            startCol.setCellValueFactory(new PropertyValueFactory<>(("startTime")));
            endCol.setCellValueFactory(new PropertyValueFactory<>(("endTime")));
            custIdCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            userCol.setCellValueFactory(new PropertyValueFactory<>("userID"));

        } catch (SQLException | ParseException throwables) {
            throwables.printStackTrace();
        }
    }

    /** This method when called changes the scene and takes the user to the 'main menu'.
     @param actionEvent Triggered by selecting the 'Cancel' button.
     RUNTIME ERROR - IOException occurs if FXML file fails to load / isn't found.*/
    public void returnToMain(ActionEvent actionEvent) throws IOException {
        try{
            toMainScreen(actionEvent);
        }
        catch(IOException e){
            fxmlErrorMessage();
        }
    }

    /** This method attempts to delete an appointment from the appointments table in the database.
     Logic check involves that the user actually selects an appointment or they are prompted to do so. The user
     is also prompted to confirm their deletion. If confirmed, a deletion SQL statement is executed removing the appointment
     from the database and an alert is generated either confirming appointment deletion or failure to do so.
     @param actionEvent Triggered by selecting the 'Delete Appointment' button.
     RUNTIME ERRORS - NullPointerException / SQLException / ParseException are handled through try / catch and prints a message to
     the console.
     LAMBDA EXPRESSION - A lambda expression is used to eliminate verbose code that comes with including Alerts. If no appointment is
     selected for deletion, the Alert takes in the relevant arguments (alert type / brief message / button type) and the lambda expression quickly
     prompts the user with cleaner and more concise code. */
    public void deleteAppointment(ActionEvent actionEvent) {
        Appointments appointment = (Appointments)appTable.getSelectionModel().getSelectedItem();
        if(appointment == null){
            Alert noSelection = new Alert(Alert.AlertType.ERROR, "No Appointment Selected!", ButtonType.OK);
            noSelection.showAndWait().filter(userAction -> userAction == ButtonType.OK); //Lambda to quickly alert & prompt the user.
        }
        else {
            Alert deleteWarning = new Alert(Alert.AlertType.CONFIRMATION);
            deleteWarning.setTitle("Delete Appointment?");
            deleteWarning.setHeaderText("Are you sure you want to delete the selected appointment?");
            deleteWarning.setContentText("Click OK to delete.");
            deleteWarning.showAndWait();
            if (deleteWarning.getResult() == ButtonType.OK) {
                try {
                    int appID = appointment.getAppointmentID();
                    AppointmentsDAO.deleteAppointment(appID);
                    appTable.getItems().clear();
                    appTable.setItems(AppointmentsDAO.getAppointments());
                    Alert deleted = new Alert(Alert.AlertType.CONFIRMATION);
                    deleted.setTitle("Appointment Deleted!");
                    deleted.setHeaderText("Appointment ID: " + appointment.getAppointmentID() + " / Type: " + appointment.getType());
                    deleted.setContentText("This appointment has been cancelled.");
                    deleted.showAndWait();
                } catch (NullPointerException | SQLException | ParseException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }


    /** This method when called changes the scene and takes the user to the 'edit appointment screen'.
     If the user fails to select an appointment, they are prompted to do so.
     @param actionEvent Triggered by selecting the 'Edit Customer' button.
     RUNTIME ERROR - IOException occurs if FXML file fails to load / isn't found.*/
    public void toEditAppointment(ActionEvent actionEvent) {
        selectedAppointment = (Appointments)appTable.getSelectionModel().getSelectedItem();
        try{
            if(selectedAppointment != null){
                if(actionEvent.getSource() == toEditAppBtn){
                    toEditAppointmentScreen(actionEvent);
                }
            }
            else{
                Alert noSelection = new Alert(Alert.AlertType.INFORMATION);
                noSelection.setTitle("No Appointment Selected!");
                noSelection.setHeaderText("Please select an appointment to edit.");
                noSelection.showAndWait();
            }
        }
        catch (IOException e){
            fxmlErrorMessage();
        }
    }

    /** This method when called changes the scene and takes the user to the 'new appointment screen'.
     @param actionEvent Triggered by selecting the 'New Appointment' button.
     RUNTIME ERROR - IOException occurs if FXML file fails to load / isn't found. */
    public void toNewAppointment(ActionEvent actionEvent) {
        try{
            toAddAppointmentScreen(actionEvent);
        }
        catch (IOException e){
            fxmlErrorMessage();
        }
    }

    /** This method when called returns the selected appointment in the table view.
     @return appointment object selected from the Appointment table. */
    public static Appointments getSelectedAppointment(){
        return selectedAppointment;
    }
}
