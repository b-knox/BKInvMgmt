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
import javafx.collections.SetChangeListener;
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
    This class controls the behavior of the Main Menu. */
public class MainMenuController implements Initializable {

    /** Declare variables to set stage and scene.
     * These variables are used to change windows in the application depending on user action.
     */

    Stage stage;
    Parent scene;

    /** Variables that hold a selection value when one exists.
     * Hold either the selected part or product from a successful search.
     */

    private static Part selectedPart;
    private static Product selectedProduct;


    /** Define the Parts TableView.
     * Display parts information in a table format with columns for ID, Name, Inventory, and Cost/Price per unit.
     */

    @FXML private TableView<Part> partTableView;
    @FXML private TableColumn<Part, Integer> partIdCol;
    @FXML private TableColumn<Part, String> partNameCol;
    @FXML private TableColumn<Part, Integer> partInvCol;
    @FXML private TableColumn<Part, Double> partCostCol;

    /** TextField for searching parts table. */

    @FXML private TextField partSearchField;


    /** Define the Products TableView.
     * Display product information in a table format with columns for ID, Name, Inventory, and Cost/Price per unit.
     */

    @FXML private TableView<Product> prodTableView;
    @FXML private TableColumn<Product, Integer> prodIdCol;
    @FXML private TableColumn<Product, String> prodNameCol;
    @FXML private TableColumn<Product, Integer> prodInvCol;
    @FXML private TableColumn<Product, Double> prodCostCol;

    /** TextField for searching products table. */

    @FXML private TextField prodSearchField;


    /** Method to display Add Part Menu on button click.
     *
     * @param event button click
     * @throws IOException
     */
    @FXML void onActionAddPart(ActionEvent event) throws IOException {

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/bk/view/AddPartMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** Getter for current selection in Main Menu Parts table.
     * @return the selected Part
     */

    public static Part getSelectedPart() {
        return selectedPart;
    }

    /** Getter for current selection in Main Menu Products table.
     * @return the selected Product
     */

    public static Product getSelectedProduct() {
        return selectedProduct;
    }

    /** Method to display Modify Part Menu on button click.
     *
     * Loads Modify Part scene and sends data from selected item to the scene for modification.
     * @param event button click
     * @throws IOException
     */
    @FXML void onActionModPart(ActionEvent event) throws IOException {

        // Set selected part for getter

        selectedPart = partTableView.getSelectionModel().getSelectedItem();

        // Load Modify Part Scene

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/bk/view/ModifyPartMenu.fxml"));
        loader.load();

        // Send selected Part information to Modify Part Screen

        ModifyPartMenuController modPartController = loader.getController();
        modPartController.sendPart(partTableView.getSelectionModel().getSelectedItem());

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }


    /** Method that Deletes a highlighted Part from the inventory.
     *
     * When Delete is pressed while a part is highlighted the user is prompted to confirm deletion.
     * Upon confirmation, the part is removed from inventory, the search field is reset and the table is updated.
     * If the User exits or pushes Cancel, the process is aborted and the search field is reset.
     * @param event
     */
    @FXML void onActionDelPart(ActionEvent event) {

        Part searchedPart = partTableView.getSelectionModel().getSelectedItem();
        String partName = partTableView.getSelectionModel().getSelectedItem().getName();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Part Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you'd like to delete " + partName + " from the database?");

        Optional<ButtonType> confirmation = alert.showAndWait();

        if (confirmation.get() == ButtonType.OK) {
            Inventory.deletePart(searchedPart);
            Platform.runLater(() -> {
                partSearchField.clear();
            });
        }
        Platform.runLater(() -> {
            partSearchField.clear();
        });
    }

    /** Method that displays Add Product Menu on button click.
     *
     * @param event button click
     * @throws IOException
     */
    @FXML void onActionAddProd(ActionEvent event) throws IOException {

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/bk/view/AddProductMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Method to display Modify Product Menu on button click.
     *
     * Loads Modify Product scene and sends data from selected item to the scene for modification.
     * @param event button click
     * @throws IOException
     */

    @FXML void onActionModProd(ActionEvent event) throws IOException {

        // Set selected product for getter

        selectedProduct = prodTableView.getSelectionModel().getSelectedItem();

        // Load Modify Part Scene

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/bk/view/ModifyProductMenu.fxml"));
        loader.load();

        // Send selected Product information to Modify Part Screen

        ModifyProductMenuController modProductController = loader.getController();
        modProductController.sendProduct(prodTableView.getSelectionModel().getSelectedItem());

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** Method that Deletes a highlighted Product from the inventory.
     *
     * When Delete is pressed while a product is highlighted the user is prompted to confirm deletion.
     * Upon confirmation, the product is removed from inventory, the search field is reset and the table is updated.
     * If the User exits or pushes Cancel, the process is aborted and the search field is reset.
     * @param event button click
     */
    @FXML void onActionDelProd(ActionEvent event) {

        Product searchedProduct = prodTableView.getSelectionModel().getSelectedItem();
        String productName = prodTableView.getSelectionModel().getSelectedItem().getName();
        ObservableList <Part> assocPartCheck = searchedProduct.getAllAssociatedParts();

        // No Product selected for deletion
        if (searchedProduct == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Unable to Complete");
            alert.setTitle(null);
            alert.setContentText("You must select a Product to delete!");
            alert.showAndWait();
        }

        // Product has associated parts
        if (assocPartCheck.size() > 0) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Deletion denied");
            alert.setTitle(null);
            alert.setContentText("Unable to delete Product with Associated Parts!");
            alert.showAndWait();
        }

        // Confirm deletion of Product
        else {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Product Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you'd like to delete " + productName + " from the database?");

            Optional<ButtonType> confirmation = alert.showAndWait();

            if (confirmation.get() == ButtonType.OK) {
                Inventory.deleteProduct(searchedProduct);
                Platform.runLater(() -> {
                    prodSearchField.clear();
                });
            }
            Platform.runLater(() -> {
                prodSearchField.clear();
            });
        }
    }

    /**
     * Method that exits application on button click.
     *
     * @param event button click
     */
    @FXML void onActionExitMain(ActionEvent event) {

        System.exit(0);
    }


    /** Initialize method for the Main Menu Controller.
     * Create/Load initial values and statements that affect the Main Menu.
     *
     * @param url the url
     * @param resourceBundle the Resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        System.out.println("Initializing Main Menu....");
        System.out.println("All Parts: " + Inventory.getAllParts().size());
        System.out.println("All Products: " + Inventory.getAllProducts().size());

        partTableView.setItems(Inventory.getAllParts());
        prodTableView.setItems(Inventory.getAllProducts());

        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        prodIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        prodNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        prodInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        prodCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Select the first item in both tables by default

        partTableView.getSelectionModel().select(0);
        prodTableView.getSelectionModel().select(0);

        // Add a listener for the Part Search text field.

        partSearchField.textProperty().addListener(new ChangeListener<String>() {

            /** Method that updates parts list as User types input.
             *
             * RUNTIME ERROR: Originally the Error message dialog was set to clear the search bar using the clear() function.  This caused an IllegalArgumentException because the listener is monitoring the text field.
             * To fix this error I utilized the runLater() function to delay clearing of the text field.
             * @param observable The text-field the listener is attached to.
             * @param oldValue The previous value of the text-field.
             * @param newValue The new value of the text-field.
             */
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                if(observable == null) {
                    partTableView.setItems(Inventory.getAllParts());
                }
                else {
                    try {

                        Integer.parseInt(newValue);
                        ObservableList<Part> searchedParts = Inventory.lookupPart(String.valueOf(newValue));
                        partTableView.setItems(searchedParts);
                        partTableView.getSelectionModel().select(0);

                        // Select exact match if found

                        if(searchedParts.size() == 1)
                            partTableView.getSelectionModel().select(0);

                        // Display error if no results are found and clear search field

                        if(searchedParts.size() == 0) {

                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText(null);
                            alert.setContentText("Your search did not return any results!");
                            alert.showAndWait();
                            Platform.runLater(() -> {
                                partSearchField.clear();
                            });
                        }

                        /* Decommissioned due to dynamic search field
                        Inventory.lookupPart(id);
                         */
                    }
                    catch (NumberFormatException e) {

                        // Display all matches of partial search

                        ObservableList<Part> searchedParts = Inventory.lookupPart(newValue.toLowerCase());
                        partTableView.setItems(searchedParts);
                        partTableView.getSelectionModel().select(0);

                        // Select exact match if found

                        if (searchedParts.size() == 1)
                            partTableView.getSelectionModel().select(0);

                        // Display error message if no results are found and clear search field

                        if(searchedParts.size() == 0) {

                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText(null);
                            alert.setContentText("Your search did not return any results!");
                            alert.showAndWait();
                            Platform.runLater(() -> {
                                partSearchField.clear();
                            });
                        }
                    }
                }
            }
        });

        // Add a listener for the Product Search text field.

        prodSearchField.textProperty().addListener(new ChangeListener<String>() {

            /** Method that updates products list as User types input.
             *
             * @param observable The text-field the listener is attached to.
             * @param oldValue The previous value of the text-field.
             * @param newValue The new value of the text-field.
             */
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                if (observable == null) {
                    prodTableView.setItems(Inventory.getAllProducts());
                }
                else {
                    try {

                        Integer.parseInt(newValue);
                        ObservableList<Product> searchedProducts = Inventory.lookupProduct(String.valueOf(newValue));
                        prodTableView.setItems(searchedProducts);

                        // Select exact match if found

                        if(searchedProducts.size() == 1)
                            prodTableView.getSelectionModel().select(0);

                        // Display error if no results are found and clear search field

                        if(searchedProducts.size() == 0) {

                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText(null);
                            alert.setContentText("Your search did not return any results!");
                            alert.showAndWait();
                            Platform.runLater(() -> {
                                prodSearchField.clear();
                            });
                            }

                        /* Decommissioned due to dynamic search field
                        Inventory.lookupProduct(id);
                         */
                    }
                    catch (NumberFormatException e) {

                        // Display all matches of partial search

                        ObservableList<Product> searchedProducts = Inventory.lookupProduct(newValue.toLowerCase());
                        prodTableView.setItems(searchedProducts);
                        prodTableView.getSelectionModel().select(0);

                        // Select exact match if found

                        if(searchedProducts.size() == 1)
                            prodTableView.getSelectionModel().select(0);

                        // Display error message if no results are found and clear search field

                        if(searchedProducts.size() == 0) {

                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText(null);
                            alert.setContentText("Your search did not return any results!");
                            alert.showAndWait();
                            Platform.runLater(() -> {
                                prodSearchField.clear();
                            });
                        }
                    }
                }

            }
        });

        // Sort Part TableView by Part ID

        partIdCol.setSortType(TableColumn.SortType.ASCENDING);
        partTableView.getSortOrder().setAll(partIdCol);

        // Sort Product TableView by Product ID

        prodIdCol.setSortType(TableColumn.SortType.ASCENDING);
        prodTableView.getSortOrder().setAll(prodIdCol);

    }

}