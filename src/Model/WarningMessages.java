package Model;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/** This class contains warning message methods. Several alerts, prompts, and other vital exception messages are found throughout
 * the application. This class offers the ability to provide customizable messages and cuts down on repeated code throughout the application. */
public class WarningMessages {

    /** This method when called creates an alert to inform the user that their was an error in attempting their desired
     * action. A string is passed in to customize the message and allow for the method to be used in a wide variety of situations.
     * @param type is a string referencing the specific object that didn't contain the necessary information.
     * LAMBDA EXPRESSION - A similar expression was used within the Appointment controller. This expression operates in an alert in the same fashion
     * in an effort to reduce lines of code and quickly prompt the user with the relevant information. */
    public static void emptyInput(String type){
        Alert alert = new Alert(Alert.AlertType.ERROR, "Missing " + type + " Information", ButtonType.OK);
        alert.showAndWait().filter(userAction -> userAction == ButtonType.OK); //Lambda to quickly alert & prompt the user.
    }

    /** When called, this method creates an alert informing the user that a selection is required but wasn't made.
     * Strings are passed in to customize the message and allow for repeated use and less code.
     * @param select is a string referring to the object that wasn't selected.
     * @param action is a string referring to the type of action that wasn't performed (i.e. delete, save, etc.) */
    public static void noSelection(String select, String action){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(" No " + select + " Selected!");
        alert.setHeaderText("Please select a " + select.toLowerCase() + " you wish to " + action + "." );
        alert.setContentText("Please try again.");
        alert.showAndWait();
    }

    /** When called, this method creates an alert informing the user that a time overlap exists when scheduling an appointment. */
    public static void appointmentOverlap(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Appointment Overlap!");
        alert.setHeaderText("An appointment already exists at that time.");
        alert.setContentText("Please check the outstanding appointments to find an available time.");
        alert.showAndWait();
    }

    /** This method when called alerts the user that their appointment cannot be scheduled on the weekend. */
    public static void isWeekendError(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Invalid appointment time!");
        alert.setHeaderText("An appointment can only be scheduled during business hours Monday - Friday.");
        alert.setContentText("Please schedule this appointment during an appropriate time.");
        alert.showAndWait();
    }

    /** Method alters user that a time cannot be set for after its supposed to start. */
    public static void invalidTimeError(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Invalid appointment time!");
        alert.setHeaderText("An appointment can not end before it is started!");
        alert.setContentText("Please check start & end dates.");
        alert.showAndWait();
    }

    /** Alerts the use that an appointment cannot be longer than 3 hours long. */
    public static void appLengthError(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Invalid appointment time!");
        alert.setHeaderText("Appointments can not be more than 3 hours long!");
        alert.setContentText("Please select a shorter appointment.");
        alert.showAndWait();
    }


    /** This method when called prints a readable error message to the console in regard to FXML
     * file not loading is able to be retrieved. */
    public static void fxmlErrorMessage(){
        System.out.println("ERROR: FXML Source File Not Found.");
    }

    /** This method when called prints a readable error message to the console in regard to SQLException error */
    public static void sqlErrorMessge(){
        System.out.println("ERROR: SQL error in interacting with the database");
    }

}

