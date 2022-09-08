package bk.Controller;

import bk.Model.InHouse;
import bk.Model.Inventory;
import bk.Model.Outsourced;
import bk.Model.Part;
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
This method controls the behavior of Modify Part Menu. */

public class ModifyPartMenuController implements Initializable {

    /** Declare variables to set stage and scene.
     These variables are used to change windows in the application depending on user action. */

    Stage stage;
    Parent scene;

    /** Variable to hold value of selected Part being modified. */

    private Part selectedPart;

    /** Create FXML objects for form field entries.
     TextFields are used to accept user input when adding a part to the application.
     Part Fields: ID (Auto Populate), Name, Inventory level, Min Inventory level, Max Inventory level, Cost/Price,
     and a Machine Number or Company Name.
     */

    @FXML private TextField modPartCostTxt;

    @FXML private TextField modPartIdTxt;

    @FXML private TextField modPartInvTxt;

    @FXML private TextField modPartMachineOrCompanyTxt;

    @FXML private TextField modPartMaxTxt;

    @FXML private TextField modPartMinTxt;

    @FXML private TextField modPartNameTxt;

    @FXML private Label modPartMachineOrCompanyLabel;

    /** Create FXML object for Radio Button selection. */

    @FXML private RadioButton modPartOutsourceRBtn;

    @FXML private ToggleGroup modPartRBtnTglGrp;

    @FXML private RadioButton modPartInHouseRBtn;

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
            modPartInvTxt.clear();
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
           modPartMinTxt.clear();
            inputValidError(3);
        }
        return validate;
    }

    /** Create Alert method to validate Modify Part form field entry.
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
                modPartMachineOrCompanyTxt.clear();
                break;
        }
    }

    /** Method that cancels Modify Part and returns to Main Menu on button click.
     *
     * @param event button click
     * @throws IOException */

    @FXML void modPartCancelButton(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you'd like to Cancel and return to the Main Menu? \nAny changes will " +
                "be discarded!");

        Optional<ButtonType> confirmation = alert.showAndWait();

        if (confirmation.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/bk/view/MainMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /** Method that saves and updates the Modified Part and returns to the Main Menu.
     *
     * @param event button click
     * @throws IOException
     */
    @FXML void modPartSaveButton(ActionEvent event) throws IOException   {

        if (modPartNameTxt.getText().isEmpty() || modPartCostTxt.getText().isEmpty() ||
                modPartInvTxt.getText().isEmpty() || modPartMinTxt.getText().isEmpty() ||
                modPartMaxTxt.getText().isEmpty())
            inputValidError(1);

        else {

            int id = selectedPart.getId();
            int inv = Integer.parseInt(modPartInvTxt.getText());
            int min = Integer.parseInt(modPartMinTxt.getText());
            int max = Integer.parseInt(modPartMaxTxt.getText());
            String name = modPartNameTxt.getText();
            double price = Double.valueOf(modPartCostTxt.getText());


            if ( validateMinMaxInput(min, max) && validateStockInput(min, max, inv)) {

                if (modPartInHouseRBtn.isSelected()) {

                    try {

                        // If Part is Outsourced deleted and create new InHouse part

                        if (selectedPart instanceof Outsourced) {

                            Inventory.deletePart(selectedPart);
                            int machine = Integer.parseInt(modPartMachineOrCompanyTxt.getText());
                            Part modifiedPart = new InHouse(id, name, price, inv, min, max, machine);
                            Inventory.updatePart(id - 101, modifiedPart);
                        }

                        // Update part if only modifying InHouse part

                        else {

                            int machine = Integer.parseInt(modPartMachineOrCompanyTxt.getText());
                            selectedPart.setId(id);
                            selectedPart.setName(name);
                            selectedPart.setStock(inv);
                            selectedPart.setPrice(price);
                            selectedPart.setMax(max);
                            selectedPart.setMin(min);
                            ((InHouse) selectedPart).setMachineId(machine);

                        }

                        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                        scene = FXMLLoader.load(getClass().getResource("/bk/view/MainMenu.fxml"));
                        stage.setScene(new Scene(scene));
                        stage.show();

                    } catch (NumberFormatException e) {
                        inputValidError(4);
                    }
                }

                if (modPartOutsourceRBtn.isSelected()) {

                    // If part is InHouse delete and create new Outsourced part

                    if (selectedPart instanceof InHouse) {

                        Inventory.deletePart(selectedPart);
                        String company = modPartMachineOrCompanyTxt.getText();
                        Part modifiedPart = new Outsourced(id, name, price, inv, min, max, company);
                        Inventory.updatePart(id - 101, modifiedPart);
                    }

                    // Update part if only modifying Outsourced part

                    else {

                        String company = modPartMachineOrCompanyTxt.getText();
                        selectedPart.setId(id);
                        selectedPart.setName(name);
                        selectedPart.setStock(inv);
                        selectedPart.setPrice(price);
                        selectedPart.setMax(max);
                        selectedPart.setMin(min);
                        ((Outsourced) selectedPart).setCompanyName(company);

                    }

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

    public void modPartInHouseRBtnSelect(ActionEvent actionEvent) {

        modPartMachineOrCompanyLabel.setText("Machine ID");
        modPartMachineOrCompanyTxt.clear();
    }

    /** Method that adds Company Name to the form for Outsourced Button.
     *
     * @param actionEvent Outsourced Radio Button selected.
     */

    public void modPartOutsourceRBtnSelect(ActionEvent actionEvent) {

        modPartMachineOrCompanyLabel.setText("Company Name");
        modPartMachineOrCompanyTxt.clear();
    }

    /** Create initialize method for Modify Part menu.
     * Creates/Loads initial values and/or statements for scene function or troubleshooting.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Set value of part being modified

        selectedPart = MainMenuController.getSelectedPart();


    }

    /** Method that receives data sent from Main Menu and populates Modify Part fields.
     *
     * Populates all attributes of the selected Part and determines if Part is In-house or Outsourced.
     * @param part the part being sent for modification
     */
    public void sendPart(Part part) {

        modPartIdTxt.setText(String.valueOf(part.getId()));
        modPartNameTxt.setText(part.getName());
        modPartInvTxt.setText(String.valueOf(part.getStock()));
        modPartCostTxt.setText(String.valueOf(part.getPrice()));
        modPartMaxTxt.setText(String.valueOf(part.getMax()));
        modPartMinTxt.setText(String.valueOf(part.getMin()));

        if(part instanceof InHouse) {

            modPartMachineOrCompanyTxt.setText(String.valueOf(((InHouse) part).getMachineId()));
            modPartMachineOrCompanyLabel.setText("Machine ID");
            modPartInHouseRBtn.setSelected(true);

        }

        if(part instanceof Outsourced) {

            modPartMachineOrCompanyTxt.setText(((Outsourced) part).getCompanyName());
            modPartMachineOrCompanyLabel.setText("Company Name");
            modPartOutsourceRBtn.setSelected(true);
        }


    }

}
