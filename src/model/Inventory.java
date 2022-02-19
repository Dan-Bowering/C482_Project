package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * @author Dan Bowering
 * C482 - Software I
 * WGU Student ID#: 000811635
 *
 *
 * Creates the inventory class.
 */
public class Inventory {

    /**
     * Creates an observable list of all parts and products in inventory.
     */
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * Initialize the Part ID and Product ID variables.
     */
    private static int partId = 0;
    private static int productId = 100;

    /**
     * Adds a part to the observable list.
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * Adds a product to the observable list.
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * Performs a text based search on the observable list of parts.
     * @param partName
     * @return namedPart
     */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> partFound = FXCollections.observableArrayList();

        for (Part part : allParts) {
            if (part.getName().contains(partName)) {
                partFound.add(part);
            }
        }
        return partFound;
    }

    /**
     * Performs a numeric based search on the observable list of parts.
     * @param partId
     * @return part
     * @return null
     */
    public static Part lookupPart(int partId) {

        Part partFound = null;

        for (Part part : allParts) {
            if (part.getId() == partId) {
                partFound = part;
            }
        }
        return partFound;
    }

    /**
     * Performs a text based search on the observable list of products.
     * @param productName
     * @return namedPart
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> productFound = FXCollections.observableArrayList();

        for (Product product : allProducts) {
            if (product.getName().contains(productName)) {
                productFound.add(product);
            }
        }
        return productFound;
    }

    /**
     * Performs a numeric based search on the observable list of parts.
     * @param productId
     * @return part
     * @return null
     */
    public static Product lookupProduct(int productId) {

        Product productFound = null;

        for (Product product : allProducts) {
            if (product.getId() == productId) {
                productFound = product;
            }
        }
        return productFound;
    }

    /**
     * Creates a new part ID.
     * @return next part ID.
     */
    public static int getNewPartId() {
        return ++partId;
    }

    /**
     * Creates a new product ID.
     * @return next product ID.
     */
    public static int getNewProductId() {
        return ++productId;
    }

    /**
     * Gets a list of all parts in inventory.
     * @return allParts.
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * Gets a list of all products in inventory.
     * @return allProducts.
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    /**
     * Deletes a part from the observable list of parts.
     * @param selectedPart
     */
    public static boolean deletePart(Part selectedPart) {
        getAllParts().remove(selectedPart);
        return true;
    }

    /**
     * Deletes a product from the observable list of products.
     * @param selectedProduct
     */
    public static boolean deleteProduct(Product selectedProduct) {
        getAllProducts().remove(selectedProduct);
        return true;
    }

    public static void updatePart (int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    public static void updateProduct (int index, Product newProduct) {
        allProducts.set(index, newProduct);
    }

}
