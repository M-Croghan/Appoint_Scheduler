package Model;

/** Facilitates the creation and manipulation of Division objects */
public class Division {

    /** Country Name */
    private int countryID;

    /** Division ID */
    private int divisionID;

    /** Division Name */
    private String divisionName;

    /** Country Name */
    private String countryName;


    /** Establishes a Division object
     * @param divisionID Division ID
     * @param divisionName Division Name
     * @param countryID Country ID*/
    public Division(int countryID, int divisionID, String divisionName) {
        this.countryID = countryID;
        this.divisionID = divisionID;
        this.divisionName = divisionName;
    }

    /** @param divID takes in a division ID
     * @return the division name associated with the ID passed to the method */
    public String Division(int divID){
        return divisionName;
    }

    /** @return country ID*/
    public int getCountryID() {
        return countryID;
    }

    /** Accepts integer parameter and sets the country ID
     * @param countryID country ID*/

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }
    /** @return division ID */
    public int getDivisionID() {
        return divisionID;
    }

    /** Accepts integer parameter and sets the division ID */
    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }
    /** @return division name */
    public String getDivisionName() {
        return divisionName;
    }

    /** Accepts string parameter and sets division name
     * @param divisionName name of the division */
    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }
}
