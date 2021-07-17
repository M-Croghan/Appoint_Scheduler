package View_Controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import static View_Controller.ScreenChange.*;

/** This class controls the functionality of the main screen of the application */
public class MainScreenController{

    /** Button when clicked exits the application */
    @FXML public Button exitBtn;

    /** Button when clicked calls a method to change the scene and takes user to the main customer screen. */
    @FXML public Button toCustomerBtn;

    /** Button when clicked calls a method to change the scene and takes user to the main appointments screen. */
    @FXML public Button appointmentsBtn;

    /** Button when clicked calls a method to change the scene and takes user to the reports screen. */
    @FXML public Button reportsBtn;

    /** Label that displays 'Welcome' message */
    @FXML public Label welcomeLbl;



    /** This method exits the application from the main screen. Selecting the exit button closes the stage and exits the application.
     @param actionEvent Selecting the exit button. */
    public void exitApp(ActionEvent actionEvent) {
        Stage stage = (Stage) exitBtn.getScene().getWindow();
        stage.close();
    }

    /** This method directs the user to the Customers menu. Selecting the Customers button changes the scene and loads the FXML file of the main screen.
     @param actionEvent Triggered by the Customers button: sets the screen title, scene, and displays the window.
     RUNTIME ERROR .load() results in an unhandled IOException in the event it cannot locate the FXML source file. Upon catching the exception an error
     message is printed to the console.*/
    public void toCustomers(ActionEvent actionEvent) {
        try{
            toCustomerScreen(actionEvent);
        }
        catch (IOException e){
            System.out.println("FXML Source File Not Found.");
        }
    }

    /** This method directs the user to the Appointments menu. Selecting the Appointments button changes the scene and loads the FXML file of the main screen.
     @param actionEvent Triggered by the Appointments button: sets the screen title, scene, and displays the window.
     RUNTIME ERROR .load() results in an unhandled IOException in the event it cannot locate the FXML source file. Upon catching the exception an error
     message is printed to the console.*/
    public void toAppointments(ActionEvent actionEvent){
        try{
            toAppointmentScreen(actionEvent);
        }
        catch (IOException e){
            System.out.println("FXML Source File Not Found.");
        }
    }

    /** This method directs the user to the Reports menu. Selecting the Reports button changes the scene and loads the FXML file of the main screen.
     @param actionEvent Triggered by the Reports button: sets the screen title, scene, and displays the window.
     RUNTIME ERROR .load() results in an unhandled IOException in the event it cannot locate the FXML source file. Upon catching the exception an error
     message is printed to the console.*/
    public void toReports(ActionEvent actionEvent) {
        try{
            toReportScreen(actionEvent);
        }
        catch (IOException e){
            System.out.println("FXML Source File Not Found.");
        }
    }
}
