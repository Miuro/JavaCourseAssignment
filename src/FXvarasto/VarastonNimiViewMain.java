package FXvarasto;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


/**
 * @author JSirkka, Miro Korhonen
 * @version 30.1.2018
 *
 */
public class VarastonNimiViewMain extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader ldr = new FXMLLoader(getClass().getResource("VarastonNimiViewView.fxml"));
            final Pane root = ldr.load();
            //final VarastonNimiViewController varastonnimiviewCtrl = (VarastonNimiViewController) ldr.getController();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("varastonnimiview.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("VarastonNimiView");
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