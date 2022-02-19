package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


/**
 * @author Dan Bowering
 * C482 - Software I
 * WGU Student ID#: 000811635
 *
 *
 * Controller class that provides controls for Add Part GUI.
 */
public class AddPartController implements Initializable {

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
        private TextField partMinText;

        @FXML
        private TextField partMachineIdText;

        @FXML
        private Button saveAddPart;

        private boolean isInHouse;

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
         * Saves the data added to the Add Part fields to the observable list.
         * @param event
         * @throws IOException
         */
        @FXML
        private void saveAddPartClicked(ActionEvent event) throws IOException {

                try {
                        String partName = partNameText.getText();
                        int partInventory = Integer.parseInt(partInvText.getText());
                        double partPrice = Double.parseDouble(partPriceText.getText());
                        int partMax = Integer.parseInt(partMaxText.getText());
                        int partMin = Integer.parseInt(partMinText.getText());
                        if (partInventory < partMin || partInventory > partMax) {

                        if (partMax < partMin) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("ERROR");
                                alert.setContentText("The minimum inventory level must be less the maximum inventory level.");
                                alert.showAndWait();
                                toAddPart(event);
                        }

                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("ERROR");
                                alert.setContentText("The current inventory level must be greater than the minimum and less than the maximum inventory thresholds.");
                                alert.showAndWait();
                                toAddPart(event);
                        }

                        else if (inHouseRadioButton.isSelected()) {
                                int machineId = Integer.parseInt(partMachineIdText.getText());
                                InHouse part = new InHouse(Inventory.getNewPartId(), partName, partPrice, partInventory, partMin, partMax, machineId);
                                Inventory.addPart(part);
                        }

                        else {
                                String companyName = partMachineIdText.getText();
                                Outsourced part = new Outsourced(Inventory.getNewPartId(), partName, partPrice, partInventory, partMin, partMax, companyName);
                                Inventory.addPart(part);
                        }

                        toMainScreen(event);
                }
                catch (NumberFormatException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("ERROR");
                        alert.setContentText("Please enter valid values in each field.");
                        alert.showAndWait();
                        toAddPart(event);

                }
                catch (NullPointerException ignored) {

                }
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
         * Loads the Main Screen controller
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
         * Loads the Add Part controller.
         * @param actionEvent
         * @throws IOException
         */
        public void toAddPart(ActionEvent actionEvent) throws IOException {
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("/view/AddPart.fxml"));
                Scene scene = new Scene(root, 600, 400);
                stage.setTitle("Add Part");
                stage.setScene(scene);
                stage.show();
        }


        /**
         * Initialize the Add Part controller.
         * @param location
         * @param resources
         */
        @Override
        public void initialize(URL location, ResourceBundle resources) {

    }
}
