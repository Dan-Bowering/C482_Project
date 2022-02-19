package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

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
 * Controller class that provides controls for Modify Part GUI.
 */
public class ModifyProductController implements Initializable {

    @FXML
    private TextField productIdText;

    @FXML
    private TextField productNameText;

    @FXML
    private TextField productInvText;

    @FXML
    private TextField productPriceText;

    @FXML
    private TextField productMaxText;

    @FXML
    private TextField productMinText;

    @FXML
    private TableView<Product> productTableView;

    @FXML
    private TableView<Part> partTableView;

    @FXML
    private TableColumn<Part, Integer> partIdColumn;

    @FXML
    private TableColumn<Part, String> partNameColumn;

    @FXML
    private TableColumn<Part, Integer> partInventoryColumn;

    @FXML
    private TableColumn<Part, Double> partPriceColumn;

    @FXML
    private TextField searchPartsField;

    @FXML
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    @FXML
    private TableView<Part> associatedPartsTableView;

    @FXML
    private TableColumn<Part, Integer> assocPartIdColumn;

    @FXML
    private TableColumn<Part, String> assocPartNameColumn;

    @FXML
    private TableColumn<Part, Integer> assocPartInvColumn;

    @FXML
    private TableColumn<Part, Double> assocPartPriceColumn;

    public Product selectedProduct;
    private static Product productToModify;

    /**
     * productToModify getter.
     * @return poductToModify
     */
    public static Product getProductToModify() {
        return productToModify;
    }


    /**
     * Performs the part search when the Part search button is clicked.
     * @param actionEvent
     */
    @FXML
    public void searchPartButtonClicked(ActionEvent actionEvent) {
        String partNameSearched = searchPartsField.getText();
        ObservableList<Part> partSearched = lookupPartName(partNameSearched);

        try {

            if (partSearched.size() == 0) {
                int partId = Integer.parseInt(partNameSearched);
                Part part = lookupPartId(partId);
                if (part != null)
                    partSearched.add(part);
                else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("INFORMATION");
                    alert.setContentText("Your search criteria did not match an existing part. Please try again.");
                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.get() == ButtonType.OK) {
                        partTableView.refresh();
                    }
                }
            }
            partTableView.setItems(partSearched);
            searchPartsField.setText("");
        }
        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("INFORMATION");
            alert.setContentText("Your search criteria did not match an existing part. Please try again.");
            alert.showAndWait();
            searchPartsField.setText("");
        }
    }

    /**
     * Saves the data added to the Modify Product form fields to the observable list.
     * @param event
     * @throws IOException
     */
    @FXML
    private void saveModifyProductClicked (ActionEvent event) throws IOException {

        selectedProduct = MainController.getProductToModify();

        try {
            int productId = selectedProduct.getId();
            String productName = productNameText.getText();
            int productInventory = Integer.parseInt(productInvText.getText());
            double productPrice = Double.parseDouble(productPriceText.getText());
            int productMax = Integer.parseInt(productMaxText.getText());
            int productMin = Integer.parseInt(productMinText.getText());

            if (productMax < productMin) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("The minimum inventory level must be less the maximum inventory level.");
                alert.showAndWait();
                toModifyProduct(event);
            }

            else if (productInventory < productMin || productInventory > productMax) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("The current inventory level must be greater than the minimum and less than the maximum inventory thresholds.");
                alert.showAndWait();
                toModifyProduct(event);
            }

            else if (associatedParts.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("CONFIRMATION");
                alert.setContentText("You are about to add a product with no associated parts. Do you want to proceed?");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.OK) {
                    Product product = new Product(productId, productName, productPrice, productInventory, productMin, productMax);
                    Inventory.addProduct(product);
                    Inventory.deleteProduct(selectedProduct);
                    toMainScreen(event);
                }
                else if (result.get() == ButtonType.CANCEL) {
                    toModifyProduct(event);
                }
            }

            else {
                Product product = new Product(productId, productName, productPrice, productInventory, productMin, productMax);

                for (Part part : associatedParts) {
                    product.addAssociatedPart(part);
                }
                Inventory.addProduct(product);
                Inventory.deleteProduct(selectedProduct);
                toMainScreen(event);
            }
        }

        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Please enter valid values in each field.");
            alert.showAndWait();
            toModifyProduct(event);
        }
        catch (NullPointerException ignored) {
        }
    }

    /**
     * Adds the part to the associated parts observable list and table view.
     * @param actionEvent
     */
    public void addAssociatedPartClicked(ActionEvent actionEvent) {
        Part selectedPart = partTableView.getSelectionModel().getSelectedItem();

        if (selectedPart != null) {
            associatedParts.add(selectedPart);
            associatedPartsTableView.setItems(associatedParts);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("You have not selected a part to associate with this product.");
            alert.showAndWait();
        }
    }

    /**
     * Removes the selected part from the associated parts observable list and table view.
     * @param actionEvent
     */
    public void removeAssociatedPartClicked(ActionEvent actionEvent) {
        Part selectedPart = associatedPartsTableView.getSelectionModel().getSelectedItem();

        if (selectedPart != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("CONFIRMATION");
            alert.setContentText("Are you sure you want to remove this part's association to the product?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                associatedParts.remove(selectedPart);
                associatedPartsTableView.setItems(associatedParts);
            }
        }
            else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("You have not selected a part to remove from the list.");
            alert.showAndWait();
        }
    }

    /**
     * Loops through the Parts list to perform a text-based search for a matching
     * Part Name.
     * @param partName
     * @return partFound
     */
    private ObservableList<Part> lookupPartName(String partName) {
        ObservableList<Part> partFound = FXCollections.observableArrayList();
        ObservableList<Part> allParts = Inventory.getAllParts();

        for (Part part : allParts) {
            if (part.getName().contains(partName)) {
                partFound.add(part);
            }
        }

        return partFound;
    }

    /**
     * Loops through the Parts list to perform an integer-based search for matching
     * Part ID.
     * @param partId
     * @return part
     * @return null
     */
    private Part lookupPartId(int partId) {
        ObservableList<Part> allParts = Inventory.getAllParts();

        for (Part part : allParts) {
            if (part.getId() == partId) {
                return part;
            }
        }

        return null;
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
     * Loads the Modify Part controller when Modify button is clicked
     * and passes the selected part data.
     * @param actionEvent
     * @throws IOException
     */
    public void toModifyProduct(ActionEvent actionEvent) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/ModifyProduct.fxml"));
        loader.load();

        productToModify = productTableView.getSelectionModel().getSelectedItem();

        if (productToModify == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("You have not selected a product from inventory to modify.");
            alert.showAndWait();
        }

        else {
            ModifyProductController mpc = loader.getController();
            mpc.sendProduct(productToModify);
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * Sets all text fields with the information of the selected product to modify
     * and sets the associated parts table view with any associated parts.
     * @param product
     */
    public void sendProduct(Product product) {

        productIdText.setText(String.valueOf(product.getId()));
        productNameText.setText(product.getName());
        productInvText.setText(String.valueOf(product.getStock()));
        productPriceText.setText(String.valueOf(product.getPrice()));
        productMaxText.setText(String.valueOf(product.getMax()));
        productMinText.setText(String.valueOf(product.getMin()));

        selectedProduct = MainController.getProductToModify();
        associatedParts = selectedProduct.getAssociatedParts();
        associatedPartsTableView.setItems(associatedParts);
    }

    /**
     * Initialize the Add Product controller and populates both table view's data.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        partTableView.setItems(Inventory.getAllParts());
        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        assocPartIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocPartInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

}