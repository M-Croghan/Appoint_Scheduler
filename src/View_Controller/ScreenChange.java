package View_Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

/** Screen change class which helps to change scenes throughout the application. This class helps in eliminating a tremendous
    amount of duplicate code throughout by allowing the methods within to be called to navigate to different screens.*/
public class ScreenChange {

    /** This method supports changing a scene within the application. When triggered by a button within the application, the method will redirect
        the user to the main appointment screen.
        @param ae denotes an action event by the user. This event involves clicking the 'Appointments' labeled button.
        RUNTIME ERROR - This method throw an IOException. A more formal try/catch block is used on the method(s) that call this method. */
    public static void toAppointmentScreen(ActionEvent ae) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(ScreenChange.class.getResource("AppointmentView.fxml")));
        Stage stage = (Stage) ((Node) ae.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1190, 320);
        stage.setTitle("Appoint Scheduler -- Add / Edit Appointments");
        stage.setScene(scene);
        stage.show();
    }
    /** This method supports changing a scene within the application. When triggered by a button within the application, the method will redirect
     the user to the main screen.
     @param ae denotes an action event by the user. This event involves clicking the 'Main Menu' labeled button.
     RUNTIME ERROR - This method throw an IOException. A more formal try/catch block is used on the method(s) that call this method. */
    public static void toMainScreen(ActionEvent ae) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(ScreenChange.class.getResource("MainScreenView.fxml")));
        Stage stage = (Stage) ((Node) ae.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 370, 215);
        stage.setTitle("Appoint Scheduler -- Welcome");
        stage.setScene(scene);
        stage.show();
    }
    /** This method supports changing a scene within the application. When triggered by a button within the application, the method will redirect
     the user to the main customer screen.
     @param ae denotes an action event by the user. This event involves clicking the 'Customers' labeled button.
     RUNTIME ERROR - This method throw an IOException. A more formal try/catch block is used on the method(s) that call this method. */
    public static void toCustomerScreen(ActionEvent ae) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(ScreenChange.class.getResource("CustomerView.fxml")));
        Stage stage = (Stage) ((Node) ae.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 720, 335);
        stage.setTitle("Appoint Scheduler -- Add / Edit Customer");
        stage.setScene(scene);
        stage.show();
    }
    /** This method supports changing a scene within the application. When triggered by a button within the application, the method will redirect
     the user to the add customer screen.
     @param ae denotes an action event by the user. This event involves clicking the 'Add Customer' labeled button.
     RUNTIME ERROR - This method throw an IOException. A more formal try/catch block is used on the method(s) that call this method. */
    public static void toAddCustomerScreen(ActionEvent ae) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(ScreenChange.class.getResource("NewCustomerView.fxml")));
        Stage stage = (Stage) ((Node) ae.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 425, 365);
        stage.setTitle("Appoint Scheduler -- Add New Customer");
        stage.setScene(scene);
        stage.show();
    }
    /** This method supports changing a scene within the application. When triggered by a button within the application, the method will redirect
     the user to the edit customer screen.
     @param ae denotes an action event by the user. This event involves clicking the 'Edit Customer' labeled button.
     RUNTIME ERROR - This method throw an IOException. A more formal try/catch block is used on the method(s) that call this method. */
    public static void toEditCustomerScreen(ActionEvent ae) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(ScreenChange.class.getResource("UpdateCustomerView.fxml")));
        Stage stage = (Stage) ((Node) ae.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 425, 365);
        stage.setTitle("Appoint Scheduler -- Update Customer");
        stage.setScene(scene);
        stage.show();
    }
    /** This method supports changing a scene within the application. When triggered by a button within the application, the method will redirect
     the user to the add appointment screen.
     @param ae denotes an action event by the user. This event involves clicking the 'Add Appointment' labeled button.
     RUNTIME ERROR - This method throw an IOException. A more formal try/catch block is used on the method(s) that call this method. */
    public static void toAddAppointmentScreen(ActionEvent ae) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(ScreenChange.class.getResource("NewAppointmentView.fxml")));
        Stage stage = (Stage) ((Node) ae.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 755, 345);
        stage.setTitle("Appoint Scheduler -- Add New Appointment");
        stage.setScene(scene);
        stage.show();
    }
    /** This method supports changing a scene within the application. When triggered by a button within the application, the method will redirect
     the user to the edit appointment screen.
     @param ae denotes an action event by the user. This event involves clicking the 'Edit Appointment' labeled button.
     RUNTIME ERROR - This method throw an IOException. A more formal try/catch block is used on the method(s) that call this method. */
    public static void toEditAppointmentScreen(ActionEvent ae) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(ScreenChange.class.getResource("EditAppointmentView.fxml")));
        Stage stage = (Stage) ((Node) ae.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 755, 345);
        stage.setTitle("Appoint Scheduler -- Edit Appointment");
        stage.setScene(scene);
        stage.show();
    }
    /** This method supports changing a scene within the application. When triggered by a button within the application, the method will redirect
     the user to the reports screen.
     @param ae denotes an action event by the user. This event involves clicking the 'Reports' labeled button.
     RUNTIME ERROR - This method throw an IOException. A more formal try/catch block is used on the method(s) that call this method. */
    public static void toReportScreen(ActionEvent ae) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(ScreenChange.class.getResource("ReportsView.fxml")));
        Stage stage = (Stage) ((Node) ae.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1140, 430);
        stage.setTitle("Appoint Scheduler -- View Reports");
        stage.setScene(scene);
        stage.show();
    }
}
