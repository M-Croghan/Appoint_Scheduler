

APPLICATION TITLE: 'Appoint Scheduler'

APPLICATION PURPOSE:
    Enables employees of an organization to quickly interact with organizational database
    in a safe and efficient way. Helps in the creation of customer records and enables the scheduling of
    appointments between the the organization and those they serve. Additional functionality includes the ability
    to quickly query the database for specific appointment information and alerts users of the application of
    upcoming appointments. This application is designed for an organization with a global reach and can enable employees
    to log-in and get to work regardless of where they are in the world.

APPLICATION AUTHOR INFORMATION:
    * Author - Michael Croghan
    * Application Version: 1.0.1
    * Date - 7/14/2021

TECHNICAL INFORMATION:
    * IDE VERSION - IntelliJ IDEA 2021.1.3 (Ultimate Edition)
    * JDK Version - jdk - 11.0.10
    * JavaFX Version - JavaFX SDK 16
                        javafx.version=16
                        javafx.runtime.version=16+8
                        javafx.runtime.build=8

DIRECTIONS FOR APPLICATION USE:
    * Start application to be prompted with the login window.
        * Username: test / Password: test - credentials to be used for testing purposes
    * The main screen offers one the ability to navigate to 3 different windows:

        1) Customers main window -- will display all available customer records in the database &
            offer the user the ability to add new, update, or delete existing customers.
        2) Appointments main window  -- will display all available appointment records in the database &
            offer the user the ability to add new, update, or delete existing appointments.
        3) Reports window -- will display an open window with the ability to execute an array of different queries.

ADDITIONAL REPORT (Part A3F):
    * The reports window allows for several types of reports to be generated. The additional reporting feature
    I chose to implement allows the user to allows for the search of all existing appointments by customer. This made logical sense
    as depending on the nature of operations of the business, a customer may routine appointments within an organization.

    As an additional feature, I elected to allow the report window to combine many of the search requirements while keeping the
    interface relatively clean. Users are able to search by: CONTACT NAME, CUSTOMER NAME, & APPOINT TYPE. They are about to search
    any of these criteria for ALL APPOINTMENTS, APPOINTMENTS IN THE UPCOMING WEEK, & APPOINTMENTS IN THE UPCOMING MONTH.
    Making no criteria selection generates all reports based on those intervals.

    Additionally, the total number of appointments found in each query are returned.

MYSQL CONNECTOR DRIVER VERSION:
    mysql-connector-java-8.0.25


