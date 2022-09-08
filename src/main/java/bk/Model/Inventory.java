package bk.Model;

/**
* @author Brandon Knox: 8/6/22 C482 Perfomance Assessment QKM2 - Inventory Management Application.
*/

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** Define Inventory Class. */

public class Inventory {

    /** Create Observable Lists for Part and Products.
     * Declare the parts Observable List as allParts.
     * Declare the products Observable List as allProducts.
     */

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();


    /** Create variables to store Unique Part and Product ID */

    private static int uniquePartId = 100;
    private static int uniqueProdId = 1000;


    /** Method to add a part to allParts Observable List
     * @param newPart the part to add
     */

    public static void addPart(Part newPart) {

    allParts.add(newPart);

    }

    /** Method to add a product to allProducts Observable List.
     * @param newProduct the product to add
     */

    public static void addProduct(Product newProduct) {

        allProducts.add(newProduct);

    }

    /** Method to search Parts by Part ID.
     *
     * Method decommissioned due to dynamic search field utilization.
     * @param partId the ID of the part
     * @return A specific part based on ID.
     */
    public static Part lookupPart(int partId) {

        Part matchedPart = null;

        for(Part sp : allParts) {
            if(sp.getId() == partId) {
                matchedPart = sp;
            }
        }

        return matchedPart;
    }



    /** Method to search Parts by using the part name
     * @param partName
     * @return A filtered list of parts based on search criteria.
     */
    public static ObservableList<Part> lookupPart(String partName) {

        ObservableList<Part> partNameSearch = FXCollections.observableArrayList();

        for (Part sp : allParts) {

            String gp = String.valueOf(sp.getId());

            if (sp.getName().toLowerCase().contains(partName) || gp.contains(partName)) {
                partNameSearch.add(sp);
            }
        }

        return partNameSearch;
    }

    /** Method to search Products by Product ID
     *
     * Method decommissioned due to dynamic search field utilization.
     * @param productId
     * @return A single Product matched by ID.
     */
    public static Product lookupProduct(int productId){

       Product matchedProduct = null;

       for(Product sp : allProducts) {
           if(sp.getId() == productId) {
               matchedProduct = sp;
           }
       }

       return matchedProduct;
    }


    /** Method to search Products by using the product name
     * @param productName
     * @return a filtered list of products based on search criteria.
     */
    public static ObservableList<Product> lookupProduct(String productName) {

        ObservableList<Product> prodNameSearch = FXCollections.observableArrayList();

        for (Product sp : allProducts) {

            String gp = String.valueOf(sp.getId());

            if (sp.getName().toLowerCase().contains(productName) || gp.contains(productName)) {
                prodNameSearch.add(sp);
            }
        }

        return prodNameSearch;
    }

    /** Method to index and update parts in the allParts Observable List.
     * @param index
     * @param selectedPart
     */
    public static void updatePart (int index, Part selectedPart) {

        allParts.set(index, selectedPart);
    }

    /** Method to index and update products in the allProducts Observable List.
     *@param index
     * @param newProduct
     */
    public static void updateProduct (int index, Product newProduct) {

        allProducts.set(index, newProduct);
    }

    /** Method to delete a selected part.
     *@param selectedPart
     * @return True if part was removed
     */
    public static boolean deletePart (Part selectedPart) {

        return allParts.remove(selectedPart);
    }

    /** Method to delete a selected product.
     * @param selectedProduct
     * @return True if product was removed
     */
    public static boolean deleteProduct (Product selectedProduct) {

        return allProducts.remove(selectedProduct);
    }

    /** Method to retrieve all parts from the allParts Observable List.
     * @return allParts
     */
    public static ObservableList<Part> getAllParts() {

        return allParts;
    }

    /** Method to retrieve all products from the allProducts Observable List.
     * @return allProducts
     */
    public static ObservableList<Product> getAllProducts() {

        return allProducts;

    }


    /** Method that retrieves and increments the generated Part ID.
     *
     * @return an auto-generated Part ID
     */
    public static int createPartId() {

        return ++uniquePartId;
    }

    /** Method that retrieves and increments the generated Product ID.
     *
     * @return an auto-generated Product ID
     */
    public static int createProdId() {

        return ++uniqueProdId;
    }


    /** Load the test data into the application.
     *
     */
    /*
   static {
        loadTestData();
    }
    */
    /** Method that defines sample data for application testing.
     *
     */
    /*
 public static void loadTestData() {

        Inventory.addPart(new InHouse(createPartId(), "Small Leg", 5.00,25, 1, 50, 10));
        Inventory.addPart(new InHouse(createPartId(), "Large Leg", 10.00, 35, 1, 100, 11));
        Inventory.addPart(new InHouse(createPartId(), "Headboard", 25.00, 15, 5, 30, 12));
        Inventory.addPart(new InHouse(createPartId(), "Chair Back", 7.50, 28, 10, 40, 13));

        Inventory.addPart(new Outsourced(createPartId(), "Tabletop", 40.00, 7, 3, 15, "Total Tops"));
        Inventory.addPart(new Outsourced(createPartId(), "Mattress", 75.00, 5, 2, 10, "Moody's Mattresses"));
        Inventory.addPart(new Outsourced(createPartId(), "Chair Arms", 2.50, 30, 2, 60, "Woodcraft Inc"));
        Inventory.addPart(new Outsourced(createPartId(), "Footboard", 15.00, 9, 2, 30, "The Footboard Co"));

        Inventory.addProduct(new Product(createProdId(), "Chair", 50.00, 3, 1, 10));
        Inventory.addProduct(new Product(createProdId(), "Table", 80.00, 5, 1, 15));
        Inventory.addProduct(new Product(createProdId(), "Bed", 150.00, 2, 1, 5));


        /*
        Part part1 = new InHouse(1, "Small Leg", 5.00, 25, 1, 50, 10);
        Inventory.addPart(part1);
        Part part2 = new InHouse(2, "Large Leg", 10.00, 35, 1, 100, 11);
        Inventory.addPart(part2);
        Part part3 = new InHouse(3, "Headboard", 25.00, 15, 5, 30, 12);
        Inventory.addPart(part3);
        Part part4 = new InHouse(4, "Chair Back", 7.50, 28, 10, 40, 13);
        Inventory.addPart(part4);
        Part part5 = new Outsourced(5, "Tabletop", 40.00, 7, 3, 15, "Total Tops");
        Inventory.addPart(part5);
        Part part6 = new Outsourced(6, "Mattress", 75.00, 5, 2, 10, "Moody's Mattresses");
        Inventory.addPart(part6);
        Part part7 = new Outsourced(7, "Chair Arms", 2.50, 30, 2, 60, "Woodcraft Inc");
        Inventory.addPart(part7);
        Part part8 = new Outsourced(8, "Footboard", 15.00, 9, 2, 30, "The Footboard Co");
        Inventory.addPart(part8);

        Product product1 = new Product(10, "Chair", 50.00, 3, 1, 10);
        Inventory.addProduct(product1);
        product1.addAssociatedPart(part1);
        product1.addAssociatedPart(part4);
        product1.addAssociatedPart(part7);

        Product product2 = new Product(11, "Table", 80.00, 5, 1, 15);
        Inventory.addProduct(product2);
        product2.addAssociatedPart(part2);
        product2.addAssociatedPart(part5);

        Product product3 = new Product(12, "Bed", 150.00, 2, 1, 5);
        Inventory.addProduct(product3);
        product3.addAssociatedPart(part1);
        product3.addAssociatedPart(part3);
        product3.addAssociatedPart(part6);
        product3.addAssociatedPart(part8);



 }
    */
}
