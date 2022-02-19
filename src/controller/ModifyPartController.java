package controller;

import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import model.*;


/**
 * @author Dan Bowering
 * C482 - Software I
 * WGU Student ID#: 000811635
 *
 *
 * Controller class that provides controls for Add Part GUI.
 */
public class ModifyPartController implements Initializable {

    @FXML
    private RadioButton inHouseRadioButton;

    @FXML
    private RadioButton outsourcedRadioButton;

    @FXML
    private ToggleGroup toggleRadioButtons;

    @FXML
    private Label machineIdLabel;

    @FXML
    private TextField partIdText;

    @FXML
    private TextField partNameText;

    @FXML
    private TextField partInvText;

    @FXML
    private TextField partPriceText;

    @FXML
    private TextField partMaxText;

    @FXML
    private TextField partMachineIdText;

    @FXML
    private TextField partMinText;

    @FXML
    private TableView<Part> partTableView;

    public Part selectedPart;

    private boolean isInHouse;

    private static Part partToModify = null;

    /**
     * partToModify getter.
     * @return partToModify
     */
    public static Part getPartToModify() {
        return partToModify;
    }

    private static Product productToModify = null;

    /**
     * productToModify getter.
     * @return poductToModify
     */
    public static Product getProductToModify() {
        return productToModify;
    }

    /**
     * Saves the modified product and all associated parts, after validating data entry.
     * @param event
     * @throws IOException
     */
    @FXML
    private void saveModifyPartClicked (ActionEvent event) throws IOException {

        selectedPart = MainController.getPartToModify();

        try {
            int partId = Integer.parseInt(partIdText.getText());
            String partName = partNameText.getText();
            int partInventory = Integer.parseInt(partInvText.getText());
            double partPrice = Double.parseDouble(partPriceText.getText());
            int partMax = Integer.parseInt(partMaxText.getText());
            int partMin = Integer.parseInt(partMinText.getText());

            if (partMax < partMin) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("The minimum inventory level must be less the maximum inventory level.");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.OK) {
                    toModifyPart(event);
                }
            }

            else if (partInventory < partMin || partInventory > partMax) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("The current inventory level must be greater than the minimum and less than the maximum inventory thresholds.");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.OK) {
                    toModifyPart(event);
                }
            }

            else if (inHouseRadioButton.isSelected()) {
                int machineId = Integer.parseInt(partMachineIdText.getText());
                InHouse part = new InHouse(partId, partName, partPrice, partInventory, partMin, partMax, machineId);
                Inventory.addPart(part);
                Inventory.deletePart(selectedPart);
            }

            else {
                String companyName = partMachineIdText.getText();
                Outsourced part = new Outsourced(partId, partName, partPrice, partInventory, partMin, partMax, companyName);
                Inventory.addPart(part);
                Inventory.deletePart(selectedPart);

            }

            toMainScreen(event);
        }
        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Please enter valid values in each field.");
            alert.showAndWait();

        }
        catch (NullPointerException ignored) {

        }
    }

    /**
     * Loads the Modify Part controller when Modify button is clicked
     * and passes the selected part data.
     * @param actionEvent
     * @throws IOException
     */
    public void toModifyPart(ActionEvent actionEvent) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/ModifyPart.fxml"));
        loader.load();

        partToModify = partTableView.getSelectionModel().getSelectedItem();

        if (partToModify == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("You have not selected a part from inventory to modify.");
            alert.showAndWait();
        }

        else {
            ModifyPartController mpc = loader.getController();
            mpc.sendPart(partToModify);
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * Sets the Machine ID/Company Name label for In-House radio button.
     * @param event
     * @throws IOException
     */
    @FXML
    private void inHouseRadioSelected(ActionEvent event) throws IOException {
        isInHouse = true;
        machineIdLabel.setText("Machine ID");
    }

    /**
     * Sets the Company Name label for Outsourced radio button.
     * @param event
     * @throws IOException
     */
    @FXML
    private void outsourcedRadioSelected(ActionEvent event) throws IOException {
        isInHouse = false;
        machineIdLabel.setText("Company Name");
    }

    /**
     * Displays cancel alert when cancel button is clicked and returns to the main screen.
     * @param event
     * @throws IOException
     */
    @FXML
    void cancelButtonClicked(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert");
        alert.setContentText("Are you sure you want to cancel?  All changes will be lost.");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            toMainScreen(event);
        }
    }

    /**
     * Returns to the main screen.
     * @param event
     * @throws IOException
     */
    @FXML
    private void toMainScreen(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Main Screen");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Sets all text fields with the information of the selected part to modify.
     * Also sets the Machine ID/Company Name field and radio buttons accordingly.
     * @param part
     */
    public void sendPart (Part part) {

        partIdText.setText(String.valueOf(part.getId()));
        partNameText.setText(part.getName());
        partInvText.setText(String.valueOf(part.getStock()));
        partPriceText.setText(String.valueOf(part.getPrice()));
        partMaxText.setText(String.valueOf(part.getMax()));
        partMinText.setText(String.valueOf(part.getMin()));

        if (part instanceof InHouse) {
            InHouse inhousePart = (InHouse) part;
            partMachineIdText.setText(String.valueOf(inhousePart.getMachineId()));
            inHouseRadioButton.setSelected(true);
        }

        else {
            Outsourced outsourcedPart = (Outsourced) part;
            machineIdLabel.setText("Company Name");
            partMachineIdText.setText(outsourcedPart.getCompanyName());
            outsourcedRadioButton.setSelected(true);
        }
    }

    /**
     * Initializes the controller.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
