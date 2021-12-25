# <img src="img\java.png" alt="java logo" width="35"/> <span style="color:red">APPOINT SCHEDULER</span> <img src="img\java.png" alt="java logo" width="35"/>

<p align="center">
<img src="img\login.png" alt="java logo" width="200"/>
</p>

## :clipboard: OVERVIEW
This project further enhances Java expertise in object-oriented programming and expands on a [previous Java application](https://github.com/M-Croghan/Inventory-Management-System) using MVC and similar technologies. It employs more intermediate aspects of Java including:  
* Lambda expressions
* Collections
* Input/output
* Advanced error handling  

Additionally, it helped build database and file server application development skills as the application integrates with a MySQL database to store appointment, user, and contact information. A data access object (DAO) design pattern was employed to create classes to access data resources and help keep the business logic seperate.

## :computer: TECHNOLOGIES USED
- Java
- SQL
- JavaFX
- SceneBuilder
- MySQL
- InteliJ IDEA

## :page_with_curl: FEATURES
Enables employees of an organization to quickly interact with an organizational database
in a safe and efficient way. Helps in the creation of customer records and enables the scheduling of
appointments between the the organization and those they serve. Additional functionality includes the ability
to quickly query the database for specific appointment information and alerts users of the application of
upcoming appointments. This application is designed for an organization with a global reach and can enable employees to log-in and get to work regardless of where they are in the world.

## :open_book: DIRECTIONS FOR APPLICATION USE:
* Start application to be prompted with the login window.
    * Username: test / Password: test - credentials to be used for testing purposes
    * All log-in attempts are recorded (both successful & failed).
        
* The main screen offers one the ability to navigate to 3 different windows:
    1) Customers main window
        - Displays all available customer records in the database &
            offers the user the ability to add new, update, or delete existing customers.
    2) Appointments main window
        - Displays all available appointment records in the database &
            offers the user the ability to add new, update, or delete existing appointments.
    3) Reports window
        - Displays a search window with the ability to execute an array of different queries.

## :bar_chart: REPORTING FEATURES:
The reports window allows for several types of reports to be generated. The additional reporting feature
I chose to implement allows the user to search for all existing appointments by customer. This made logical sense
as depending on the nature of operations of the business, a customer may make routine appointments within an organization.  

As an additional feature, I elected to allow the report window to combine many of the search requirements while keeping the interface relatively clean. Users are able to search by:
* Contact Name
* Customer Name
* Appointment Type  

They are able to search any of these criteria for:
* ALL APPOINTMENTS
* APPOINTMENTS IN THE UPCOMING WEEK
* APPOINTMENTS IN THE UPCOMING MONTH  

Making no criteria selection generates all reports based on those intervals.
Additionally, the total number of appointments found in each query are returned.

## :bulb: LESSONS LEARNED
This was the first time using a MySQL database with an application I developed. Understanding and using JDBC was a challenge as was thinking through how my prepared statements would produce the desired SQL queries.  

The greatest challenge came in developing user stories to truly gain an accurate view of what the application was to accomplish. I learned more about effective planning and design in the development of this application than on any previous work.