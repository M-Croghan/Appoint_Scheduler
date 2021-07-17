package Main;

import DAO.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ResourceBundle;

/** Application main class */
public class Main extends Application {

    /** Sets the primary stage for the application and displays the log-in screen to the user.
     * @param primaryStage .
     * @throws Exception .*/
    @Override
    public void start(Stage primaryStage) throws Exception{
        ResourceBundle rb = ResourceBundle.getBundle("properties/lang");

        Parent main = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../View_Controller/LoginView.fxml"));
            loader.setResources(rb);
            main = loader.load();

            Scene scene = new Scene(main);

            primaryStage.setScene(scene);
            primaryStage.setTitle("Appoint Scheduler");

            primaryStage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /** Entry point to the application. Establishes initial database connection and closes the connection when application closes. */
    public static void main(String[] args) {

        Connection conn = DBConnection.startConnection();

        launch(args);

        DBConnection.closeConnection();
    }


}
