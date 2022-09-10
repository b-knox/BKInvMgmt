package bk.Model;

/**
 * @author Brandon Knox: 8/6/22 C482 Perfomance Assessment QKM2 - Inventory Management Application.
 */

/** Define InHouse Class  */

public class InHouse extends Part {

    /** Declare private variables  */
    private int machineId;

    /** Create InHouse constructor and call superclass variables.
     *
     * @param id part id
     * @param name part name
     * @param price part price
     * @param stock part stock level
     * @param min part minimum stock level
     * @param max part maximum stock level
     * @param machineId part machineID
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /** Define setters and getters.  */

    /**
     *
     * @param machineId machine id
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    /**
     *
     * @return the machine ID
     */
    public int getMachineId() {
        return machineId;
    }

}
