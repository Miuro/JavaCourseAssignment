package pyoravarasto;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Pyora;
import model.Vuokraus;
import view.UusiTulostusDialogController;
import view.UusiVuokrausDialogController;
import view.VarastoOverviewController;

public class MainApp extends Application {
	
	private Stage primaryStage;
	private BorderPane overview;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Pyörävuokraamo");
		
		initRootLayout();
	}

	/**
	 * Luodaan perusikkuna, eli varastoOverview.
	 */
	public void initRootLayout() {
		try {
			// Ladataan perusnäkymä eli varastoOverview
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/view/VarastoOverview.fxml"));
			overview = (BorderPane) loader.load();
			
			// Näytetään scene / perusnäkymä
			Scene scene = new Scene(overview);
			primaryStage.setScene(scene);
			primaryStage.show();
			VarastoOverviewController controller = loader.getController();
			controller.setMainApp(this);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Luonti vuokrausikkunalle
	 * @param pyora Pyörä, mitä vuokrataa
	 * @return True, jos pyörä vuokrattiin onnistuneesti
	 */
	public boolean showUusiVuokrausDialog(Pyora pyora, Vuokraus vuokraus) {
		try {
			// Ladataan fxml-tiedosto
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainApp.class.getResource("/view/UusiVuokrausDialog.fxml"));
	        BorderPane page = (BorderPane) loader.load();
	        
	        // Luodaan uusi stage, ja asetetaan sen sisällöksi (sceneksi) tuo loaderin lataama page.
	        // Kikkaillaan modalitylla.
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Vuokraus");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(primaryStage);
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);
	        dialogStage.setTitle("Uusi vuokraus");
	        
	        // Haetaan controller ja yhdistetään se stagen kanssa.
	        UusiVuokrausDialogController controller = loader.getController();
	        controller.setDialogStage(dialogStage);
	        controller.asetaPyora(pyora);
	        controller.asetaVuokraus(vuokraus);
	        
	        dialogStage.showAndWait();
	        return controller.onkoOK();
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Luonti tulostusdialogille
	 * */
	/*public void showUusiTulostusDialog() {
		try {
			// Ladataan fxml-tiedosto ja luodaan uusi stage sille
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainApp.class.getResource("/view/UusiTulostusDialog.fxml"));
	        BorderPane page = (BorderPane) loader.load();
	        
	        // Luodaan uusi stage, ja asetetaan sen sisällöksi (sceneksi) tuo loaderin lataama page.
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Tulostus");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(primaryStage);
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);
	        
	        // Haetaan controller ja yhdistetään se stagen kanssa.
	        //UusiTulostusDialogController controller = loader.getController();
	        //controller.setDialogStage(dialogStage);
	        
	        dialogStage.showAndWait();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}*/

	/**
	 * Maini käynnistää vehkeen
	 * @param args parametrit
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
