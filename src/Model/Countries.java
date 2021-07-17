package Model;

/** Facilitates the creation and manipulation of Country objects */
public class Countries {

    /** The country's ID */
    private int countryID;

    /** The country's name*/
    private String countryName;


    /** Country constructor */
    public Countries(int countryID, String countryName) {
        this.countryID = countryID;
        this.countryName = countryName;
    }

    /** Retrieves a country ID
     * @return country's ID*/
    public int getCountryID() {
        return countryID;
    }

    /** Accepts integer parameter and sets country ID
     * @param countryID */
    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }
    /** Retrieves a country name
     * @return country's name*/
    public String getCountryName() {
        return countryName;
    }

    /** Accepts integer parameter and sets country name
     * @param countryName */
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
