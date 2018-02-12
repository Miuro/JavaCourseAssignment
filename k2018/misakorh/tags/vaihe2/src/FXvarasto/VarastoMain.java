package FXvarasto;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


/**
 * @author JSirkka, Miro Korhonen
 * @version 30.1.2018
 *
 */
public class VarastoMain extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader ldr = new FXMLLoader(getClass().getResource("VarastoView.fxml"));
            final Pane root = ldr.load();
            //final VarastoController varastoCtrl = (VarastoController) ldr.getController();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("varasto.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Varasto");
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