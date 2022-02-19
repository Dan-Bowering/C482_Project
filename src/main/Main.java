package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Product;

/**
 * @author Dan Bowering
 * C482 - Software I
 * WGU Student ID#: 000811635
 * JavaDocs folder is located in "C482 Project/src/JavaDoc"
 * <p>
 * An Inventory Management application that tracks Parts and Products information and allows
 * data manipulation.
 * </p>
 * <p><b>
 * FUTURE ENHANCEMENT - A future enhancement I would implement would be to improve the search functionality to
 * automatically populate any part/product into the appropriate table view as the end user types in the field,
 * rather than having to type search characters and click the Search button to return the results.
 * </b></p>
 */
public class Main extends Application {

    /**
     * Creates the stage and initializes the Main Screen.
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
        primaryStage.setTitle("Main Screen");
        primaryStage.setScene(new Scene(root, 850, 600));
        primaryStage.show();
    }


    public static void main(String[] args) {

        /**
         * Create sample parts and add them to the observable list/program.
         */
        int partId = Inventory.getNewPartId();
        InHouse solidStateDrive = new InHouse(partId, "Solid State Drive", 89.99, 13, 3, 20, 101);

        partId = Inventory.getNewPartId();
        InHouse ramCard = new InHouse(partId, "RAM Card", 39.99, 20, 1, 20, 101);

        partId = Inventory.getNewPartId();
        InHouse motherboard = new InHouse(partId, "Motherboard", 119.99, 7, 1, 15, 102);

        partId = Inventory.getNewPartId();
        Outsourced hardDiskDrive =  new Outsourced(partId, "Hard Disk Drive", 59.99, 5, 1, 10, "SanDisk");

        partId = Inventory.getNewPartId();
        Outsourced powerSupply = new Outsourced(partId, "Power Supply", 89.99, 3, 1, 10, "EVGA");

        Inventory.addPart(solidStateDrive);
        Inventory.addPart(ramCard);
        Inventory.addPart(motherboard);
        Inventory.addPart(hardDiskDrive);
        Inventory.addPart(powerSupply);

        /**
         * Create sample products and add them to the observable list/program.
         */
        int productId = Inventory.getNewProductId();
        Product laptop = new Product(productId, "Laptop Computer", 799.99, 9, 1, 30);

        productId = Inventory.getNewProductId();
        Product desktop = new Product(productId, "Desktop Computer", 599.99, 14, 1, 20);

        Inventory.addProduct(laptop);
        Inventory.addProduct(desktop);

        launch(args);
    }
}
