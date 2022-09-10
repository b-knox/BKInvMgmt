package bk.Controller;

import bk.Model.Inventory;
import bk.Model.Part;
import bk.Model.Product;
import javafx.application.Platform;
import javafx.beans.property.Property;
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
import java.util.Optional;
import java.util.ResourceBundle;

/** @author Brandon Knox: 8/6/22 C482 Perfomance Assessment QKM2 - Inventory Management Application
This class controls the behavior of the Add Product Menu. */

public class AddProductMenuController implements Initializable {

    /** Declare variables to set stage and scene.
     These variables are used to change windows in the application depending on user action. */

    Stage stage;
    Parent scene;

    /** Create placeholder list for Associated parts.
     * This list will hold associated parts in the current add session and either save or discord depending on user input.
     */

    private ObservableList<Part> tempAssocParts = FXCollections.observableArrayList();

    /** Define the available Parts TableView.
     * Display parts information in a table format with columns for ID, Name, Inventory, and Cost/Price per unit.
     */

    @FXML private TableView<Part> addProdAvailPartTableView;
    @FXML private TableColumn<Part, Integer> addProdPartIdCol;
    @FXML private TableColumn<Part, String> addProdPartNameCol;
    @FXML private TableColumn<Part, Integer> addProdPartInvCol;
    @FXML private TableColumn<Part, Double> addProdPartCostCol;

    /** Create TextField object to search available Parts. */

    @FXML private TextField addProdSearchTxt;

    /** Define the Associated Parts TableView.
     * Display Product part information in a table format with columns for ID, Name, Inventory, and Cost/Price per unit.
     */

    @FXML private TableView<Part> addProdAssocPartsTableView;
    @FXML private TableColumn<Part, Integer> assocPartIdCol;
    @FXML private TableColumn<Part, String> assocPartNameCol;
    @FXML private TableColumn<Part, Integer> assocPartInvCol;
    @FXML private TableColumn<Part, Double> assocPartCostCol;

    /** Create FXML objects for form field entries.
     TextFields are used to accept user input when adding a product to the application.
     Product Fields: ID (Auto Populate), Name, Inventory level, Min Inventory level, Max Inventory level, and Price.
     */

    @FXML private TextField addProdIdField;
    @FXML private TextField addProdNameTxt;
    @FXML private TextField addProdInvTxt;
    @FXML private TextField addProdMinTxt;
    @FXML private TextField addProdMaxTxt;
    @FXML private TextField addProdPriceTxt;

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
            addProdInvTxt.clear();
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
            addProdMinTxt.clear();
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

            // No Part Selected for add
            case 4:
                alert.setHeaderText("Unable to Complete");
                alert.setTitle(null);
                alert.setContentText("You must select an available Part to add!");
                alert.showAndWait();
                break;

            // No Part selected for removal or no Associated Parts
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
    @FXML void addProdAddAssocPartButton(ActionEvent event) {

        Part selectedPart = addProdAvailPartTableView.getSelectionModel().getSelectedItem();

        if (selectedPart == null)
            inputValidError(4);
        else {
            tempAssocParts.add(selectedPart);
            addProdAssocPartsTableView.setItems(tempAssocParts);
            addProdAssocPartsTableView.getSelectionModel().select(0);
        }
    }


    /** Method that cancels Add Product and returns to Main Menu on button click.
     *
     * @param event
     * @throws IOException
     */

    @FXML void addProdMenuCancelButton(ActionEvent event) throws IOException {

        // If nothing is Input return the User to the Main Menu

        if (addProdNameTxt.getText().isEmpty() && addProdInvTxt.getText().isEmpty() && addProdPriceTxt.getText().isEmpty()
        && addProdMinTxt.getText().isEmpty() && addProdMaxTxt.getText().isEmpty()) {

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
                tempAssocParts.clear();
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/bk/view/MainMenu.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }
    }

    /** Method that saves a Product and returns to the Main Menu after saving.
     *
     * @param event
     * @throws IOException
     */

    @FXML void addProdSaveProductButton(ActionEvent event) throws IOException {

        if (addProdNameTxt.getText().isEmpty() || addProdInvTxt.getText().isEmpty() || addProdPriceTxt.getText().isEmpty()
        || addProdMinTxt.getText().isEmpty() || addProdMaxTxt.getText().isEmpty())
            inputValidError(1);

        else {
            int id = Inventory.createProdId();
            int inv = Integer.parseInt(addProdInvTxt.getText());
            int min = Integer.parseInt(addProdMinTxt.getText());
            int max = Integer.parseInt(addProdMaxTxt.getText());
            String name = addProdNameTxt.getText();
            double price = Double.valueOf(addProdPriceTxt.getText());

            if (validateMinMaxInput(min, max) && validateStockInput(min, max, inv)) {

                Inventory.addProduct(new Product(id, name, price, inv, min, max));
                for (Part p : tempAssocParts) {
                    Inventory.lookupProduct(id).addAssociatedPart(p);
                }

                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/bk/view/MainMenu.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();

            }
        }
    }

    /** Method to remove an associated part from a product.
     *
     * @param event
     */
    @FXML void removeAssociatedPartButton(ActionEvent event) {

        if (tempAssocParts.size() == 0 || addProdAssocPartsTableView.getSelectionModel().getSelectedItem() == null) {
            inputValidError(5);
        }
        else {
            Part selectedPart = addProdAssocPartsTableView.getSelectionModel().getSelectedItem();
            String partName = addProdAssocPartsTableView.getSelectionModel().getSelectedItem().getName();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Remove Associated Part");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you'd like to remove the associated part, " + partName + ", from this product?");

            Optional<ButtonType> confirmation = alert.showAndWait();

            if (confirmation.get() == ButtonType.OK) {

                tempAssocParts.remove(selectedPart);
                addProdAssocPartsTableView.setItems(tempAssocParts);
                addProdAssocPartsTableView.getSelectionModel().select(0);
            }
        }
    }

    /** Create initialize method for Add Product Menu.
     * Creates/Loads initial values and/or statements for scene function or troubleshooting.
     * @param url the url
     * @param resourceBundle the Resource bundle
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        System.out.println("Initializing Add Product Menu....");

        // Initialize and populate available Parts table

        addProdAvailPartTableView.setItems(Inventory.getAllParts());

        addProdPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        addProdPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addProdPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addProdPartCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Initialize Associated Parts table

        // tempAssocParts.clear();
        addProdAssocPartsTableView.setItems(tempAssocParts);

        assocPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPartCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        addProdAvailPartTableView.getSelectionModel().select(0);
        addProdAssocPartsTableView.getSelectionModel().select(0);

        // Add a listener for the Part Search text field.

        addProdSearchTxt.textProperty().addListener(new ChangeListener<String>() {

            /** Method that updates parts list as User types input.
             *
             * @param observable The text-field the listener is attached to.
             * @param oldValue The previous value of the text-field.
             * @param newValue The new value of the text-field.
             */
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                if(observable == null) {
                    addProdAvailPartTableView.setItems(Inventory.getAllParts());
                }
                else {
                    try {

                        Integer.parseInt(newValue);
                        ObservableList<Part> searchedParts = Inventory.lookupPart(String.valueOf(newValue));
                        addProdAvailPartTableView.setItems(searchedParts);
                        addProdAvailPartTableView.getSelectionModel().select(0);

                        // Select exact match if found

                        if(searchedParts.size() == 1)
                            addProdAvailPartTableView.getSelectionModel().select(0);

                        // Display error if no results are found and clear search field

                        if(searchedParts.size() == 0) {

                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText(null);
                            alert.setContentText("Your search did not return any results!");
                            alert.showAndWait();
                            Platform.runLater(() -> {
                                addProdSearchTxt.clear();
                            });
                        }
                    }
                    catch (NumberFormatException e) {

                        // Display all matches of partial search

                        ObservableList<Part> searchedParts = Inventory.lookupPart(newValue.toLowerCase());
                        addProdAvailPartTableView.setItems(searchedParts);
                        addProdAvailPartTableView.getSelectionModel().select(0);

                        // Select exact match if found

                        if (searchedParts.size() == 1)
                            addProdAvailPartTableView.getSelectionModel().select(0);

                        // Display error message if no results are found and clear search field

                        if(searchedParts.size() == 0) {

                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText(null);
                            alert.setContentText("Your search did not return any results!");
                            alert.showAndWait();
                            Platform.runLater(() -> {
                                addProdSearchTxt.clear();
                            });
                        }
                    }
                }
            }
        });

        // Sort Part TableView by Part ID

        addProdPartIdCol.setSortType(TableColumn.SortType.ASCENDING);
        addProdAvailPartTableView.getSortOrder().setAll(addProdPartIdCol);

        // Sort Associated Parts TableView by Part ID

        assocPartIdCol.setSortType(TableColumn.SortType.ASCENDING);
        addProdAssocPartsTableView.getSortOrder().setAll(assocPartIdCol);


    }
}
