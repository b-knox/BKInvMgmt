package bk.Controller;

import bk.Model.Inventory;
import bk.Model.Part;
import bk.Model.Product;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

/** @author Brandon Knox: 8/6/22 C482 Perfomance Assessment QKM2 - Inventory Management Application
This class controls the behavior of Modify Product Menu. */
public class ModifyProductMenuController implements Initializable {

    /** Declare variables to set stage and scene.
     These variables are used to change windows in the application depending on user action. */
    Stage stage;
    Parent scene;

    /** Variable to hold value of the Product being modified. */

    private Product selectedProduct;

    /** Create placeholder list for Associated parts.
     * This list will hold associated parts in the current add session and either save or discord depending on user input.
     */

     private ObservableList<Part> modAssocParts = FXCollections.observableArrayList();

     /** The part that's modified by adding/removing to Associated Parts. */
     private Part modPart;

     /** The Product that's being modified. */

    private Product modifiedProduct;

    /** Define the available Parts TableView.
     * Display parts information in a table format with columns for ID, Name, Inventory, and Cost/Price per unit.
     */

    @FXML private TableView<Part> modProdAvailPartTableView;

    @FXML private TableColumn<Part, Double> modProdPartCostCol;

    @FXML private TableColumn<Part, Integer> modProdPartIdCol;

    @FXML private TableColumn<Part, Integer> modProdPartInvCol;

    @FXML private TableColumn<Part, String> modProdPartNameCol;

    /** Define the Associated Parts TableView.
     * Display Product part information in a table format with columns for ID, Name, Inventory, and Cost/Price per unit.
     */

    @FXML private TableView<Part> modProdAssocPartsTableView;

    @FXML private TableColumn<Part, Double> modProdAssocPartCostCol;

    @FXML private TableColumn<Part, Integer> modProdAssocPartIdCol;

    @FXML private TableColumn<Part, Integer> modProdAssocPartInvCol;

    @FXML private TableColumn<Part, String> modProdAssocPartNameCol;

    /** Create FXML objects for form field entries.
     TextFields are used to accept user input when adding a product to the application.
     Product Fields: ID (Auto Populate), Name, Inventory level, Min Inventory level, Max Inventory level, and Price.
     */

    @FXML private TextField modProdIdField;

    @FXML private TextField modProdInvTxt;

    @FXML private TextField modProdMaxTxt;

    @FXML private TextField modProdMinTxt;

    @FXML private TextField modProdNameTxt;
    @FXML private TextField modProdPriceTxt;

    @FXML private TextField modProdSearchTxt;

    /** Create method that validates the value input for Inventory field.
     *
     * Compare Stock level input and return true if value is in range between min and max input values.
     * If validation fails, clear inventory field value and display error message to user.
     *
     * @param min user input minimum level
     * @param max user input maximum level
     * @param inv user input current inventory level
     * @return boolean value
     */
    private boolean validateStockInput (int min, int max, int inv) {

        boolean validate = true;

        if(inv > max || inv < min) {
            validate = false;
            modProdInvTxt.clear();
            inputValidError(2);
        }
        return validate;
    }

    /** Create method that validates the value input for Minimum field.
     *
     * Compare minimum stock level to maximum stock and return true if minimum is greater than 0 and less than maximum.
     * If validation fails, clear minimum inventory field and display error message to user.
     *
     * @param min user input minimum level
     * @param max user input maximum level
     * @return boolean value
     */
    private boolean validateMinMaxInput (int min, int max) {

        boolean validate = true;

        if (min <= 0 || min >= max) {
            validate = false;
            modProdMinTxt.clear();
            inputValidError(3);
        }
        return validate;
    }

    /** Create Alert method to validate Add Product form field entry.
     *
     * Displayed error is determined by which invalidation error is triggered.
     * @param inputType the # of the invalid input message
     */
    private void inputValidError (int inputType) {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        switch (inputType) {

            // Attempting to save part without completing all fields
            case 1:
                alert.setTitle("Error");
                alert.setHeaderText("Incomplete Form Fields");
                alert.setContentText("All fields require values to add!");
                alert.showAndWait();
                break;

            // Inventory level is outside of range of Min & Max values
            case 2:
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Stock Level");
                alert.setContentText("Inventory value must be a number between input for Min and Max fields!");
                alert.showAndWait();
                break;

            // Minimum level is greater than Max, blank, or negative
            case 3:
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Minimum Level");
                alert.setContentText("Minimum level of stock must be greater than 0 and less than Maximum stock level!");
                alert.showAndWait();
                break;

            case 4:
                alert.setHeaderText("Unable to Complete");
                alert.setTitle(null);
                alert.setContentText("You must select an available Part to add!");
                alert.showAndWait();
                break;

            case 5:
                alert.setHeaderText("Unable to Complete");
                alert.setTitle(null);
                alert.setContentText("No part is selected for removal, or there are no associated parts to remove.");
                alert.showAndWait();

        }
    }

    /** Method that adds a highlighted Part from the Parts Table to the temporary Associated Parts List and TableView.
     *
     * @param event Add button clicked
     */
    @FXML void modProdAddAssocPartButton(ActionEvent event) {

        modPart = modProdAvailPartTableView.getSelectionModel().getSelectedItem();

        if (modPart == null)
            inputValidError(4);
        else {

            modAssocParts.add(modPart);
            modProdAssocPartsTableView.setItems(modAssocParts);
            System.out.println("Item Added: " + modPart.getName());
            System.out.println("Assoc Parts Size: " + modAssocParts.size());
        }
    }

    /** Method that cancels Modify Product and returns to Main Menu on button click.
     *
     * @param event cancel button click
     * @throws IOException */

    @FXML void modProdMenuCancelButton(ActionEvent event) throws IOException {

        // If nothing is Input return the User to the Main Menu

        if (modProdNameTxt.getText().isEmpty() && modProdInvTxt.getText().isEmpty() && modProdPriceTxt.getText().isEmpty()
                && modProdMinTxt.getText().isEmpty() && modProdMaxTxt.getText().isEmpty()) {

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/bk/view/MainMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
        else {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Cancel Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you'd like to Cancel and return to the Main Menu? \nAll fields will be " +
                    "cleared and information will be lost!");

            Optional<ButtonType> confirmation = alert.showAndWait();

            if (confirmation.get() == ButtonType.OK) {

                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/bk/view/MainMenu.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }
    }

    /** Method to remove an associated part from a product.
     *
     * @param event remove button click
     */
    @FXML
    void modProdRemoveAssociatedPartButton(ActionEvent event) {

       modPart = modProdAssocPartsTableView.getSelectionModel().getSelectedItem();

        if (modAssocParts.size() == 0 || modPart == null)
            inputValidError(5);

        else {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Remove Associated Part");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you'd like to remove the associated part, " + modPart.getName() +
                    ", from this product?");

            Optional<ButtonType> confirmation = alert.showAndWait();

            if (confirmation.get() == ButtonType.OK) {

                modAssocParts.remove(modPart);
                modProdAssocPartsTableView.setItems(modAssocParts);
                System.out.println("Item removed: " + modPart.getName());
                System.out.println(modAssocParts.size());
            }
        }
    }

    /** Method that saves changes to a Product and returns to the Main Menu after saving.
     *
     * @param event save button click
     * @throws IOException
     */
    @FXML
    void modProdSaveProductButton(ActionEvent event) throws IOException {

        int min = Integer.parseInt(modProdMinTxt.getText());
        int max = Integer.parseInt(modProdMaxTxt.getText());
        int inv = Integer.parseInt(modProdInvTxt.getText());


        if (modProdNameTxt.getText().isEmpty() || modProdInvTxt.getText().isEmpty() || modProdPriceTxt.getText().isEmpty()
                || modProdMinTxt.getText().isEmpty() || modProdMaxTxt.getText().isEmpty())
            inputValidError(1);

        if (validateMinMaxInput(min, max) && validateStockInput(min, max, inv)) {

            int id = selectedProduct.getId();
            String name = modProdNameTxt.getText();
            double price = Double.valueOf(modProdPriceTxt.getText());
            ObservableList <Part> tempAssocParts = FXCollections.observableArrayList();

            modifiedProduct = new Product (id, name, price, inv, min, max);
            Inventory.addProduct(modifiedProduct);
            Inventory.deleteProduct(selectedProduct);

            for (Part p : modAssocParts) {
                tempAssocParts.add(p);
            }
            for (Part part : tempAssocParts) {
                    Inventory.lookupProduct(id).addAssociatedPart(part);
            }

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/bk/view/MainMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /** Create initialize method for Modify Product Menu.
     * Creates/Loads initial values and/or statements for scene function or troubleshooting.
     * @param url the url
     * @param resourceBundle the Resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        System.out.println("Initializing Modify Product Menu....");

        // Set value of product being modified

        selectedProduct = MainMenuController.getSelectedProduct();
        modProdIdField.setText(String.valueOf(selectedProduct.getId()));
        modProdNameTxt.setText(selectedProduct.getName());
        modProdInvTxt.setText(String.valueOf(selectedProduct.getStock()));
        modProdPriceTxt.setText(String.valueOf(selectedProduct.getPrice()));
        modProdMinTxt.setText(String.valueOf(selectedProduct.getMin()));
        modProdMaxTxt.setText(String.valueOf(selectedProduct.getMax()));
        modAssocParts = Inventory.lookupProduct(selectedProduct.getId()).getAllAssociatedParts();


        System.out.println("Associated Parts Size: " + modAssocParts.size());
        // Initialize and populate available Parts table

        modProdAvailPartTableView.setItems(Inventory.getAllParts());

        modProdPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        modProdPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        modProdPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modProdPartCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Initialize Associated Parts table

        modProdAssocPartsTableView.setItems(modAssocParts);
        modProdAssocPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        modProdAssocPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        modProdAssocPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modProdAssocPartCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        modProdAvailPartTableView.getSelectionModel().select(0);
        modProdAssocPartsTableView.getSelectionModel().select(0);

        // Add a listener for the Part Search text field.

        modProdSearchTxt.textProperty().addListener(new ChangeListener<String>() {

            /** Method that updates parts list as User types input.
             *
             * @param observable The text-field the listener is attached to.
             * @param oldValue The previous value of the text-field.
             * @param newValue The new value of the text-field.
             */
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                if(observable == null) {
                    modProdAvailPartTableView.setItems(Inventory.getAllParts());
                }
                else {
                    try {

                        Integer.parseInt(newValue);
                        ObservableList<Part> searchedParts = Inventory.lookupPart(String.valueOf(newValue));
                        modProdAvailPartTableView.setItems(searchedParts);
                        modProdAvailPartTableView.getSelectionModel().select(0);

                        // Select exact match if found

                        if(searchedParts.size() == 1)
                            modProdAvailPartTableView.getSelectionModel().select(0);

                        // Display error if no results are found and clear search field

                        if(searchedParts.size() == 0) {

                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText(null);
                            alert.setContentText("Your search did not return any results!");
                            alert.showAndWait();
                            Platform.runLater(() -> {
                                modProdSearchTxt.clear();
                            });
                        }
                    }
                    catch (NumberFormatException e) {

                        // Display all matches of partial search

                        ObservableList<Part> searchedParts = Inventory.lookupPart(newValue.toLowerCase());
                        modProdAvailPartTableView.setItems(searchedParts);
                        modProdAvailPartTableView.getSelectionModel().select(0);

                        // Select exact match if found

                        if (searchedParts.size() == 1)
                            modProdAvailPartTableView.getSelectionModel().select(0);

                        // Display error message if no results are found and clear search field

                        if(searchedParts.size() == 0) {

                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText(null);
                            alert.setContentText("Your search did not return any results!");
                            alert.showAndWait();
                            Platform.runLater(() -> {
                                modProdSearchTxt.clear();
                            });
                        }
                    }
                }
            }
        });

        // Sort Part TableView by Part ID

        modProdPartIdCol.setSortType(TableColumn.SortType.ASCENDING);
        modProdAvailPartTableView.getSortOrder().setAll(modProdPartIdCol);

        // Sort Associated Parts TableView by Part ID

        modProdAssocPartIdCol.setSortType(TableColumn.SortType.ASCENDING);
        modProdAssocPartsTableView.getSortOrder().setAll(modProdAssocPartIdCol);
    }

    /** Method that receives data sent from Main Menu and populates Modify Product fields.
     *
     * Populates all attributes of the selected Product and any associated Parts.
     * @param product the product being sent for modification
     */
    public void sendProduct(Product product) {

        modProdIdField.setText(String.valueOf((product.getId())));
        modProdNameTxt.setText(product.getName());
        modProdInvTxt.setText(String.valueOf(product.getStock()));
        modProdPriceTxt.setText(String.valueOf(product.getPrice()));
        modProdMaxTxt.setText(String.valueOf(product.getMax()));
        modProdMinTxt.setText(String.valueOf(product.getMin()));
    }
}
