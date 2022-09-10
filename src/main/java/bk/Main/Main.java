package bk.Main;

import bk.Model.Part;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/** @author Brandon Knox: 8/6/22 C482 Perfomance Assessment QKM2 - Inventory Management Application
 * This class contains the main method for the application.
 * <p><b>
 * FUTURE ENHANCEMENT: There are several enhancements that could be made to this application, such as:
 * Building an underlying database of parts and products that update dynamically as parts are consumed or products are sold.
 * Adding calculated fields that display the number of Products able to be made based off of current Part stock.
 * Adding alerts/triggers that flag when Stock level of a Part or Product is low and needs to be Ordered or Produced.
 * </b></p>
 */

public class Main extends Application {

    /** Load the Main Menu scene when the program starts.
     *
     * @param stage The Main Menu
     * @throws IOException the exception
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/bk/View/MainMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 450);
        stage.setScene(scene);
        stage.show();
    }

    /** The main method for the application.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {

               launch();

    }

}