package model;


/**
 * @author Dan Bowering
 * C482 - Software I
 * WGU Student ID#: 000811635
 *
 *
 * Creates the InHouse class and extend the Part class.
 */
public class InHouse extends Part{

    private int machineId;

    /**
     * Constructor for the InHouse class object.
     * @param id,name,price,stock,min,max,machineId
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * machineId getter
     * @return machineId
     */
    public int getMachineId() {
        return machineId;
    }

    /**
     * machineId setter
     * @param machineId
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

}
