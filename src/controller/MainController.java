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
import javafx.scene.control.TextField;
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
 * Controller class that provides controls for Main Screen GUI.
 */
public class MainController implements Initializable {

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
    private TableView<Product> productTableView;

    @FXML
    private TableColumn<Product, Integer> productIdColumn;

    @FXML
    private TableColumn<Product, String> productNameColumn;

    @FXML
    private TableColumn<Product, Integer> productInventoryColumn;

    @FXML
    private TableColumn<Product, Double> productPriceColumn;

    @FXML
    private TextField searchProductsField;

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


//////////////////  PARTS CONTROLLERS AND EVENTS  //////////////////

    /**
     * Loads the Add Part controller when the Add button is clicked.
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
     * Deletes a part from inventory when selected and the Delete button is clicked.
     * @param actionEvent
     */
    public void deletePartClicked (ActionEvent actionEvent) {

        Part selectedPart = partTableView.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("You have not selected a part from inventory to delete.");
            alert.showAndWait();
        }

        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Alert");
            alert.setContentText("Are you sure you want to delete this part from inventory?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                Inventory.deletePart(selectedPart);
            }
            Inventory.getAllParts();
        }
    }

    /**
     * Performs the part search when the Part search button is clicked.
     * @param actionEvent
     */
    @FXML
    public void searchPartButtonClicked (ActionEvent actionEvent) {

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
                        searchPartsField.setText("");
                    }
                }
            }
            partTableView.setItems(partSearched);
            searchPartsField.setText("");
        }
        catch(NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("INFORMATION");
                alert.setContentText("Your search criteria did not match an existing part. Please try again.");
                alert.showAndWait();
                searchPartsField.setText("");
            }
    }

    /**
     * Loops through the Parts list to perform a text-based search for a matching
     * Part Name.
     * @param partName
     * @return partFound
     */
    private ObservableList<Part> lookupPartName (String partName) {

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
    private Part lookupPartId (int partId) {

        ObservableList<Part> allParts = Inventory.getAllParts();

        for (Part part : allParts) {
            if (part.getId() == partId) {
                return part;
            }
        }
        return null;
    }

//////////////////  PRODUCTS CONTROLLERS AND EVENTS  //////////////////

    /**
     * Loads the Add Product controller when the Add button is clicked.
     * @param actionEvent
     * @throws IOException
     *
     * RUNTIME ERROR - After initially creating the AddProduct.fxml file and trying to load the controller, I
     * was receiving a java.lang.ClassNotFoundException and the controller would not load.  Looking at the cause
     * breakpoint, I determined that the fx:controller was incorrectly named.  After changing it to match the .fxml
     * file name, the controller loaded without issue.
     */
    public void toAddProduct (ActionEvent actionEvent) throws IOException {

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddProduct.fxml"));
        Scene scene = new Scene(root, 900, 630);
        stage.setTitle("Add Product");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Loads the Modify Product controller when Modify button is clicked
     * and passes the selected product data.
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
     * Deletes a product from inventory when selected and the Delete button is clicked.
     * Validates that a product is selected from the table view and checks for associated parts.
     * @param actionEvent
     */
    public void deleteProductClicked (ActionEvent actionEvent) {

        Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();
        ObservableList<Part> associatedParts = selectedProduct.getAssociatedParts();

        if (selectedProduct == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("You have not selected a product from inventory to delete.");
            alert.showAndWait();
        }

        else if (!(associatedParts.isEmpty())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("You cannot delete a product with associated parts from inventory.");
            alert.showAndWait();
        }

        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("CONFIRMATION");
            alert.setContentText("Are you sure you want to delete this product from inventory?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                Inventory.deleteProduct(selectedProduct);
            }
        }
        Inventory.getAllProducts();
    }

    /**
     * Performs the product search when the Product search button is clicked.
     * @param actionEvent
     */
    @FXML
    public void searchProductButtonClicked (ActionEvent actionEvent) {

        String productNameSearched = searchProductsField.getText();
        ObservableList<Product> productSearched = lookupProductName(productNameSearched);

        try {
            if (productSearched.size() == 0) {
            int productId = Integer.parseInt(productNameSearched);
            Product product = lookupProductId(productId);
            if (product != null)
                productSearched.add(product);
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
        productTableView.setItems(productSearched);
        searchProductsField.setText("");
        }

        catch(NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("INFORMATION");
            alert.setContentText("Your search criteria did not match an existing product. Please try again.");
            alert.showAndWait();
        }
    }

    /**
     * Loops through the Products list to perform a text-based search for a matching
     * Product Name.
     * @param productName
     * @return partFound
     */
    private ObservableList<Product> lookupProductName (String productName) {

        ObservableList<Product> productFound = FXCollections.observableArrayList();
        ObservableList<Product> allProducts = Inventory.getAllProducts();

        for (Product product : allProducts) {
            if (product.getName().contains(productName)) {
                productFound.add(product);
            }
        }
        return productFound;
    }

    /**
     * Loops through the Products list to perform an integer-based search for matching
     * Product ID.
     * @param productId
     * @return part
     * @return null
     */
    private Product lookupProductId (int productId) {

        ObservableList<Product> allProducts = Inventory.getAllProducts();

        for (Product product : allProducts) {
            if (product.getId() == productId) {
                return product;
            }
        }
        return null;
    }

    /**
     * Exits the program when the Exit button is clicked.
     * @param event
     */
    @FXML
    void exitButtonClicked(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert");
        alert.setContentText("Are you sure you want to exit the program?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    /**
     * Initialize the main screen controller and populate the table view data.
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

        productTableView.setItems(Inventory.getAllProducts());
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }



}
