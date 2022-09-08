package bk.Model;

/**
 * @author Brandon Knox: 8/6/22 C482 Perfomance Assessment QKM2 - Inventory Management Application.
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** Define Product Class */
public class Product {

    /** Create Observable List to store parts that are associated with Products.
     */
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /** Declare private variables. */
   private int id;
   private String name;
   private double price;
   private int stock;
   private int min;
   private int max;

    /** Create Product constructor.
     * @param id product id
     * @param name product name
     * @param price product price/cost per unit
     * @param stock product stock level
     * @param min product minimum stock level
     * @param max product maximum stock level
     */
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }


    /** Define setters and getters. */

    /**
     * @param id set the id
     */
   public void setId(int id) {
       this.id = id;
   }

    /**
     * @param name set the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @param price set the price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     *
     * @param stock set the stock level
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     *
     * @param min set the minimum inventory level
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     *
     * @param max set the maximum inventory level
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     *
     * @return the stock level
     */
    public int getStock() {
        return stock;
    }

    /**
     *
     * @return the min stock level
     */
    public int getMin() {
        return min;
    }

    /**
     *
     * @return the max stock level
     */
    public int getMax() {
        return max;
    }

    /** Add Associated Parts to an Observable List.
     *
     * @param part the part to add to the list
     */
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

    /** Delete Associated Part from the Observable List.
     *
     * @param selectedAssociatedPart the part to delete
     * @return true or false
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {

        return associatedParts.remove(selectedAssociatedPart);
    }

    /** Getter method for returning list of associated parts.
     *
     * @return Observable List containing associated parts
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }
}
