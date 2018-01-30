package FXvarasto;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


/**
 * @author JSirkka
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