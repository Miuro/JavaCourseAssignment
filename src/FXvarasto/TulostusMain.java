package FXvarasto;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


/**
 * @author Omistaja
 * @version 30.1.2018
 *
 */
public class TulostusMain extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader ldr = new FXMLLoader(getClass().getResource("TulostusView.fxml"));
            final Pane root = ldr.load();
            //final TulostusController tulostusCtrl = (TulostusController) ldr.getController();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("tulostus.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Tulostus");
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args Ei käytössä
     */
    public static void main(String[] args) {
        launch(args);
    }
}