# APPOINT SCHEDULER - DESKTOP APPOINTMENT SCHEDULING APPLICATION
## TECHNOLOGIES USED
- Java
- JavaFX
- Scenebuilder
- MySQL
- InteliJ IDEA

## APPLICATION PURPOSE:
Enables employees of an organization to quickly interact with organizational database
in a safe and efficient way. Helps in the creation of customer records and enables the scheduling of
appointments between the the organization and those they serve. Additional functionality includes the ability
to quickly query the database for specific appointment information and alerts users of the application of
upcoming appointments. This application is designed for an organization with a global reach and can enable employees
to log-in and get to work regardless of where they are in the world.

## DIRECTIONS FOR APPLICATION USE:
* Start application to be prompted with the login window.
    * Username: test / Password: test - credentials to be used for testing purposes
    * All log-in attempts are recorded (both successful & failed).
        
* The main screen offers one the ability to navigate to 3 different windows:
    1) Customers main window -- will display all available customer records in the database &
            offer the user the ability to add new, update, or delete existing customers.
    2) Appointments main window  -- will display all available appointment records in the database &
            offer the user the ability to add new, update, or delete existing appointments.
    3) Reports window -- will display an open window with the ability to execute an array of different queries.

## REPORTING FEATURES:
The reports window allows for several types of reports to be generated. The additional reporting feature
I chose to implement allows the user to search for all existing appointments by customer. This made logical sense
as depending on the nature of operations of the business, a customer may make routine appointments within an organization.

 As an additional feature, I elected to allow the report window to combine many of the search requirements while keeping the
 interface relatively clean. Users are able to search by: CONTACT NAME, CUSTOMER NAME, & APPOINT TYPE. They are about to search
 any of these criteria for ALL APPOINTMENTS, APPOINTMENTS IN THE UPCOMING WEEK, & APPOINTMENTS IN THE UPCOMING MONTH.
 Making no criteria selection generates all reports based on those intervals.

 Additionally, the total number of appointments found in each query are returned.
