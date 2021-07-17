package View_Controller;

import DAO.AppointmentsDAO;
import DAO.UserDAO;
import Model.Appointments;
import Model.User;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.util.ResourceBundle;

import static DAO.AppointmentsDAO.upcomingAppointments;
import static DAO.UserDAO.getUserName;
import static Model.Appointments.displayBusinessHours;
import static Model.WarningMessages.fxmlErrorMessage;
import static View_Controller.ScreenChange.toMainScreen;

/** Controller class for the Log-in Screen */
public class LoginController implements Initializable {
    /** Exit button when clicked closes the application */
    @FXML public Button exitButton;

    /** Login button when clicked logs the users information changes the scene to main screen */
    @FXML public Button loginButton;

    /** Label which displays the users current location based on systems time/date settings */
    @FXML public Label userLoc;

    /** Label which prompts user for username. Can be translated based on user's system language settings */
    @FXML public Label userLbl;

    /** Label which prompts user for password. Can be translated based on user's system language settings */
    @FXML private Label pwLbl;

    /** Label that displays 'Location'. Can be translated based on user's system language settings */
    @FXML public Label locLbl;

    /** Text field which allows for user to enter password */
    @FXML public TextField pwText;

    /** Text feild which allows for user to enter username  */
    @FXML public TextField userText;

    /** String which stores the user name of the current user of the application */
    public static String currentUser;

    /** String which stores the user ID of the current user of the application */
    public static int currentUserID;

    /** ResourceBundle which aids in the translation of various text / labels on the login screen */
    private ResourceBundle rb;

    /** Identifies the users current zone ID */
    private ZoneId zoneId = ZoneId.systemDefault();

    /** Initializes the  Login screen. Translates visible text depending on the user's system settings
        RUNTIME ERROR - NullPointerException is handled through try/catch in the event that the resourcebundle helping
        with text translation cannot be found. */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rb = rb;
        try{
            exitButton.setText(rb.getString("Exit"));
            loginButton.setText(rb.getString("Login"));
            userLoc.setText(zoneId.toString());
            userLbl.setText(rb.getString("Username"));
            pwLbl.setText(rb.getString("password"));
            locLbl.setText(rb.getString("Location"));

        }
        catch (NullPointerException e){
            System.out.println(e.getMessage());
        }
    }

    /** This method closes the application.
        @param actionEvent Triggered by clicking the 'Exit Button' */
    public void exitApp(ActionEvent actionEvent) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    /** This method validates the users credentials by checking them against records in the database.
        If username and password are valid, the users username and ID are stored and the user is allowed access to the
        main screen of the application. If the user enters the wrong credentials, the user is informed and prompted to try again.
        Login attempts are stored in a user access log and tracks successful / failed log-ins, username, date and time of attempt, and user location.
        Additionally, once the user logins in, the database is checked for any appointments upcoming in the next 15 minutes. If found, the user is alerted to the
        specific appointment. If no appointments are upcoming, the user is informed as such.
        @param actionEvent Triggered by clicking on the 'Login' button.
        @throws SQLException the users credentials are checked against database records. The SQL statement that does this have the potential for error if its syntax is incorrect
        RUNTIME ERROR - IOException occurs if the FXML file isn't found or doesn't load after successful log-in and is handled via try/catch. */
    public void login(ActionEvent actionEvent) throws SQLException {

        try{
            if (UserDAO.validateUser(userText.getText(), pwText.getText())){
                ObservableList<Appointments> userAppointments = AppointmentsDAO.upcomingAppointments();
                toMainScreen(actionEvent);
                User.logUser(userText.getText());
                currentUser = userText.getText();
                currentUserID = UserDAO.getUserID(userText.getText());

                if(!userAppointments.isEmpty()){

                    for(int i = 0; i < userAppointments.size(); i++){
                        int userId = userAppointments.get(i).getUserID();
                        int appId = userAppointments.get(i).getAppointmentID();
                        String appTitle = userAppointments.get(i).getTitle();
                        String appStarting = userAppointments.get(i).getStartTime();

                        if(userId == currentUserID){
                            Alert upcoming = new Alert(Alert.AlertType.INFORMATION);
                            upcoming.setTitle("Upcoming Appointment!");
                            upcoming.setHeaderText("Welcome " + getUserName(userId));
                            upcoming.setContentText("Appointment ID: " + appId + " / " + appTitle + " is starting  at " + appStarting + "!");
                            upcoming.showAndWait();
                        }
                        else{
                            Alert upcoming = new Alert(Alert.AlertType.INFORMATION);
                            upcoming.setTitle("No Upcoming Appointments.");
                            upcoming.setHeaderText("There are currently no appointments starting within the next 15 minutes.");
                            upcoming.setContentText("Please select appointments to view upcoming appointment details.");
                            upcoming.showAndWait();
                        }
                    }

                }
                else{
                    Alert upcoming = new Alert(Alert.AlertType.INFORMATION);
                    upcoming.setTitle("No Upcoming Appointments.");
                    upcoming.setHeaderText("There are currently no appointments starting within the next 15 minutes.");
                    upcoming.setContentText("Please select appointments to view upcoming appointment details.");
                    upcoming.showAndWait();
                }

            }
            else{
                User.failedLogIn(userText.getText());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(rb.getString("invalid"));
                alert.setHeaderText(rb.getString("incorrect"));
                alert.setContentText(rb.getString("tryagain"));
                alert.showAndWait();
                pwText.clear();
                userText.clear();
            }
        }
        catch (IOException e){
            fxmlErrorMessage();
        }
    }

    /** This method returns the current user's username.
     @return the user name of the user after successful login */
    public static String getCurrentUser(){
        return currentUser;
    }

    /** This method returns the current user's user ID.
     @return the user ID of the user after successful login */
    public static int getCurrentUserID(){
        return currentUserID;
    }

}
