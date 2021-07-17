package Model;

/** Facilitates the creation and manipulation of Contact objects */
public class Contacts {

    /** contact ID property */
    private int contactID;

    /** contact name property */
    private String contactName;

    /** contact email property */
    private String contactEmail;

    /** Contact Constructor */
    public Contacts(int contactID, String contactName, String contactEmail) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }


    /** Accepts string parameter and sets Contact's name
     * @param contactName */
    public Contacts(String contactName) {
        this.contactName = contactName;
    }


    /** Retrieves a contact ID
     * @return contacts's ID*/
    public int getContactID() {
        return contactID;
    }


    /** Accepts integer parameter and sets Contact's ID
     * @param contactID */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }


    /** Retrieves a contact name
     * @return contacts's ID*/
    public String getContactName() {
        return contactName;
    }

    /** Accepts string parameter and sets Contact's name
     * @param contactName */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /** Retrieves a contact email
     * @return contacts's email*/
    public String getContactEmail() {
        return contactEmail;
    }

    /** Accepts string parameter and sets Contact's email
     * @param contactEmail */
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
}
