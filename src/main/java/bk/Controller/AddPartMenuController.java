package bk.Controller;

import bk.Model.InHouse;
import bk.Model.Inventory;
import bk.Model.Outsourced;
import bk.Model.Part;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/** @author Brandon Knox: 8/6/22 C482 Perfomance Assessment QKM2 - Inventory Management Application
    This method controls the behavior of the Add Part Menu. */
public class AddPartMenuController implements Initializable {

    /** Declare variables to set stage and scene.
     These variables are used to change windows in the application depending on user action. */

    Stage stage;
    Parent scene;

    /** Create FXML objects for form field entries.
     TextFields are used to accept user input when adding a part to the application.
     Part Fields: ID (Auto Populate), Name, Inventory level, Min Inventory level, Max Inventory level, Cost/Price,
     and a Machine Number or Company Name.
     */

    @FXML private TextField addPartCostTxt;

    @FXML private TextField addPartIdTxt;

    @FXML private TextField addPartInvTxt;

    @FXML private TextField addPartMachineOrCompanyTxt;

    @FXML private TextField addPartMaxTxt;

    @FXML private TextField addPartMinTxt;

    @FXML private TextField addPartNameTxt;

    /** Create FXML object for label that changes based on radio button selection. */

    @FXML private Label addPartMachineOrCompanyLabel;

    /** Create FXML object for Radio Button selection. */

    @FXML private RadioButton addPartInHouseRBtn;

    @FXML private RadioButton addPartOutsourceRBtn;

    @FXML private ToggleGroup addPartRBtnTglGrp;

    /** Create method that validates the value input for Inventory field.
     *
     * Compare Stock level input and return true if value is in range between min & max input values.
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
            addPartInvTxt.clear();
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
            addPartMinTxt.clear();
            inputValidError(3);
        }
        return validate;
    }

    /** Method that cancels Add Part and returns to Main Menu on button click.
     *
     * If no input is detected on the form, Cancel will return to Main Menu without confirmation.
     * If any input has been entered in the form, including selecting In-house or Outsourced, the user will be prompted to confirm before exit.
     *
     * @param event
     * @throws IOException */

    @FXML void addPartCancelButton(ActionEvent event) throws IOException {

        // If nothing is Input return the User to the Main Menu

        if (addPartNameTxt.getText().isEmpty() && addPartCostTxt.getText().isEmpty() && addPartInvTxt.getText().isEmpty() &&
                addPartMinTxt.getText().isEmpty() && addPartMaxTxt.getText().isEmpty() && !(addPartInHouseRBtn.isSelected())
                && !(addPartOutsourceRBtn.isSelected())) {

            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
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
                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/bk/view/MainMenu.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }
    }

    /** Create Alert method to validate Add Part form field entry.
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
                alert.setContentText("You must select an In-House or Outsourced Part and complete all fields to add!");
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

            // Machine ID is not entered as a number
            case 4:
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Machine ID");
                alert.setContentText("Please enter a numeric Machine ID!");
                alert.showAndWait();
                addPartMachineOrCompanyTxt.clear();
                break;
        }
    }

    /** RUNTIME ERROR: I initially tried to define the Variables 'Machine' and 'Company' before the if statements for Radio Button Selection.
     * The program compiled and allowed creation of In-house Parts, but returns a NumberFormatException when entering a
     * String value for the Company Name of Outsourced Parts.
     *
     * Method that saves and adds a Part to the inventory and returns to the Main Menu.
     *
     * @param event save part button clicked
     * @throws IOException
     */
    @FXML void addPartSaveButton(ActionEvent event) throws IOException {

        if (!(addPartInHouseRBtn.isSelected() || addPartOutsourceRBtn.isSelected()) || addPartNameTxt.getText().isEmpty() ||
                addPartCostTxt.getText().isEmpty() || addPartInvTxt.getText().isEmpty() ||
                addPartMinTxt.getText().isEmpty() || addPartMaxTxt.getText().isEmpty())
            inputValidError(1);

        else {
            int id = Inventory.createPartId();
            int inv = Integer.parseInt(addPartInvTxt.getText());
            int min = Integer.parseInt(addPartMinTxt.getText());
            int max = Integer.parseInt(addPartMaxTxt.getText());
            String name = addPartNameTxt.getText();
            double price = Double.valueOf(addPartCostTxt.getText());

            if ( validateMinMaxInput(min, max) && validateStockInput(min, max, inv)) {

                if (addPartInHouseRBtn.isSelected()) {

                    try {

                        int machine = Integer.parseInt(addPartMachineOrCompanyTxt.getText());
                        Inventory.addPart(new InHouse(id, name, price, inv, min, max, machine));

                        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                        scene = FXMLLoader.load(getClass().getResource("/bk/view/MainMenu.fxml"));
                        stage.setScene(new Scene(scene));
                        stage.show();

                    } catch (NumberFormatException e) {
                        inputValidError(4);
                    }
                }

                if (addPartOutsourceRBtn.isSelected()) {

                    String company = addPartMachineOrCompanyTxt.getText();
                    Inventory.addPart(new Outsourced(id, name, price, inv, min, max, company));

                    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("/bk/view/MainMenu.fxml"));
                    stage.setScene(new Scene(scene));
                    stage.show();
                }
            }
        }
    }


    /** Method that adds Machine ID to the form for In-House Button.
     *
     * @param actionEvent In-House Radio Button selected.
     */
    public void addPartInHouseClick(ActionEvent actionEvent) {

        addPartMachineOrCompanyLabel.setText("Machine ID");
    }


    /** Method that adds Company Name to the form for Outsourced Button.
     *
     * @param actionEvent Outsourced Radio Button selected.
     */
    public void addPartOutsourceClick(ActionEvent actionEvent) {

        addPartMachineOrCompanyLabel.setText("Company Name");
    }




    /** Create initialize method for Add Part menu.
     * Creates/Loads initial values and/or statements for scene function or troubleshooting.
     * @param url
     * @param resourceBundle
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


}
