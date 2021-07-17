package Model;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Date;
import java.sql.Timestamp;

/** Facilitates the creation and manipulation of Customer objects */
public class Customer {

    /** Customer ID property*/
    private int customerID;

    /** Customer Name property*/
    private String customerName;

    /** Customer Address property*/
    private String address;

    /** Customer Postal Code property*/
    private String zip;

    /** Customer Phone property*/
    private String phone;

    /** Customer creation date */
    private Date createDate;

    /** User who created the customer */
    private String createdBy;

    /** Timestamp of when the customer was last updated */
    private Timestamp lastUpdate;

    /** User who last updated the customer */
    private String updateby;

    /** Customers division ID */
    private int divID;

    /** List which holds customer objects */
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

    /** Creates a customer objects and sets corresponding fields */
    public Customer(int customerID, String customerName, String address, String zip, String phone, Date createDate, String createdBy, Timestamp lastUpdate, String updateby, int divID) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.address = address;
        this.zip = zip;
        this.phone = phone;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.updateby = updateby;
        this.divID = divID;
    }

    /** Accepts string parameter and sets customer name*/
    public Customer(String customerName) {
        this.customerName = customerName;
    }

    /** Empty customer constructor */
    public Customer() {

    }

    /** Retrieves a customer ID
     * @return customer's ID*/
    public int getCustomerID() {
        return customerID;
    }

    /** Accepts integer parameter and sets customer ID
     * @param customerID */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /** Retrieved customer name
     * @return the customers name*/
    public String getCustomerName() {
        return customerName;
    }

    /** Accepts String parameter and sets customer name
     * @param customerName */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /** Retrieves customer address
     * @return customer's address */
    public String getAddress() {
        return address;
    }

    /** Accepts string parameter and sets customer address
     * @param address */
    public void setAddress(String address) {
        this.address = address;
    }

    /** Retrieves customer postal code
     * @return the zip code / postal code of the customer */
    public String getZip() {
        return zip;
    }

    /** Accepts string parameter and sets customer postal code
     * @param zip */
    public void setZip(String zip) {
        this.zip = zip;
    }

    /** Retrieves customer phone number
     * @return phone number of the customer */
    public String getPhone() {
        return phone;
    }

    /** Accepts string parameter and sets customer phone number
     * @param phone */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /** Retrieves customer creation date
     * @return date customer was created */
    public Date getCreateDate() {
        return createDate;
    }

    /** Accepts date parameter and sets when customer was created
     * @param createDate */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /** Retrieves who created the customer
     * @return user who created the customer */
    public String getCreatedBy() {
        return createdBy;
    }

    /** Accepts string parameter and sets who created the customer
     * @param createdBy */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /** Retrieves last time customer was updated
     * @return last update time */
    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    /** Accepts timestamp parameter and when the customer was last updated
     * @param lastUpdate */
    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /** Retrieves user who updated customer
     * @return who lasted updated*/
    public String getUpdateby() {
        return updateby;
    }

    /** Accepts string parameter and sets who last updated the customer
     * @param updateby */
    public void setUpdateby(String updateby) {
        this.updateby = updateby;
    }

    /** Retrieves customer Division ID
     * @return division ID */
    public int getDivID() {
        return divID;
    }

    /** Accepts integer parameter and sets customer division ID
     * @param divID*/
    public void setDivID(int divID) {
        this.divID = divID;
    }

    /** Retrieves the list of customer objects
     * @return list of customers */
    public static ObservableList<Customer> getAllCustomers(){
        return allCustomers;
    }
}
