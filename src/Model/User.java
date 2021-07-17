package Model;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/** Facilitates the creation and manipulation of User objects */
public class User {
    /** User ID */
    private int userId;

    /** User Name */
    private String userName;

    /** User Password */
    private String password;

    /** Establishes a user object
      * @param userId
     * @param userName
     * @param password */
    public User(int userId, String userName, String password) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
    }

    /** 2nd user constructor  which accepts and sets 2 parameters
     * @param userName  username
     * @param password  user password */
    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public User(String userName) {
        this.userName = userName;
    }


    /** This method records a users successful login attempt to a user access log.
     * @param user passes in username to store in the log. */
    public static void logUser(String user) throws IOException {
        FileWriter recordLogin = new FileWriter("login_activity.txt", true);
        PrintWriter output = new PrintWriter(recordLogin);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        ZoneId userZone =  ZoneId.systemDefault();
        output.println("*** SUCCESSFUL *** User: " + user + " || Logged In: " + dtf.format(now) + " || From Location:" + userZone);
        recordLogin.close();
    }

    /** This method records a users failed login attempt to a user access log.
     * @param user passes in username to store in the log. */
    public static void failedLogIn(String user) throws IOException {
        FileWriter recordLogin = new FileWriter("login_activity.txt", true);
        PrintWriter output = new PrintWriter(recordLogin);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        ZoneId userZone =  ZoneId.systemDefault();
        output.println("*** FAILED ATTEMPT *** User: " + user + " || Logged In: " + dtf.format(now) + " || From Location:" + userZone);
        recordLogin.close();
    }

    //USER GETTERS & SETTERS

    /**  @return the user's ID */
    public int getUserId() {
        return userId;
    }

    /** Sets the User's ID
     * @param userId User ID stored as an integer */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**  @return the user's username */
    public String getUserName() {
        return userName;
    }

    /** Sets the User's name
     * @param userName User name stored as a string */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**  @return the user's password */
    public String getPassword() {
        return password;
    }

    /** Sets the User's name
     * @param password User password stored as a string */
    public void setPassword(String password) {
        this.password = password;
    }


}
