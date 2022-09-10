package bk.Model;

/**
 * @author Brandon Knox: 8/6/22 C482 Perfomance Assessment QKM2 - Inventory Management Application.
 */

/** Define Outsourced Class  */
public class Outsourced extends Part {

    /** Define private variables  */
    private String companyName;

    /** Create Outsourced constructor and call superclass variables.
     *
     * @param id part id
     * @param name part name
     * @param price part price
     * @param stock part stock level
     * @param min part minimum stock level
     * @param max part maximum stock level
     * @param companyName the Company Name
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /** Create setters and getters  */

    /**
     *
     * @param companyName the Company Name
     */
    public void setCompanyName(String companyName) {

        this.companyName = companyName;
    }

    /**
     *
     * @return the Company Name
     */
    public String getCompanyName() {
        return companyName;
    }

}
