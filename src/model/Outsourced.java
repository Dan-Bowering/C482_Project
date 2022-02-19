package model;


/**
 * @author Dan Bowering
 * C482 - Software I
 * WGU Student ID#: 000811635
 *
 *
 * Creates the Outsourced class and extend the Part class.
 */
public class Outsourced extends Part{

    String companyName;

    /**
     * Constructor for the Outsourced class object.
     * @param id,name,price,stock,min,max,companyName
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * companyName getter
     * @return companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * companyName setter
     * @aparm companyName
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

}
