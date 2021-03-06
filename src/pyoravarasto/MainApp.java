package pyoravarasto;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Asiakas;
import model.Pyora;
import model.Vuokraus;
import view.UusiVuokrausDialogController;
import view.VarastoOverviewController;


/**
 * JavaFX:n p��luokka. T�m�n kautta avataan ikkunat ja v�litet��n tietoa ikkunoiden v�lill�.
 * @author Jouko Sirkka
 * @version 1.1, 23.5.2018
 */
public class MainApp extends Application {
	
	private Stage primaryStage;
	private BorderPane overview;

	@Override
	public void start(Stage primaryStage1) {
		this.primaryStage = primaryStage1;
		this.primaryStage.setTitle("Py�r�vuokraamo");
		
		initRootLayout();
	}



	/**
	 * Luodaan perusikkuna, eli varastoOverview.
	 */
	public void initRootLayout() {
		try {
			// Ladataan perusn�kym� eli varastoOverview
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/view/VarastoOverview.fxml"));
			overview = (BorderPane) loader.load();
			
			// N�ytet��n scene / perusn�kym�
			Scene scene = new Scene(overview);
			primaryStage.setScene(scene);
			primaryStage.show();
			VarastoOverviewController controller = loader.getController();
			controller.setMainApp(this);
			primaryStage.setOnCloseRequest(e -> controller.voikoSulkea());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * Luonti vuokrausikkunalle
	 * @param pyora Py�r�, mit� vuokrataa
	 * @param vuokraus Vuokraus, mit� k�sitell��n vuokrauksessa.
	 * @param asiakas Asiakas, mit� k�sitell��n vuokrauksessa.
	 * @return True, jos py�r� vuokrattiin onnistuneesti
	 */
	public int showUusiVuokrausDialog(Pyora pyora, Vuokraus vuokraus, Asiakas asiakas) {
		try {
			// Ladataan fxml-tiedosto
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainApp.class.getResource("/view/UusiVuokrausDialog.fxml"));
	        BorderPane page = (BorderPane) loader.load();
	        
	        // Luodaan uusi stage, ja asetetaan sen sis�ll�ksi (sceneksi) tuo loaderin lataama page.
	        // Kikkaillaan modalitylla.
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Vuokraus");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(primaryStage);
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);
	        dialogStage.setTitle("Uusi vuokraus");
	        
	        // Haetaan controller ja yhdistet��n se stagen kanssa.
	        UusiVuokrausDialogController controller = loader.getController();
	        controller.setDialogStage(dialogStage);
	        controller.asetaPyora(pyora);
	        controller.asetaVuokraus(vuokraus);
	        controller.asetaAsiakas(asiakas);
	        
	        dialogStage.showAndWait();
	        return controller.mitaTehtiin();
		}
		catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
	}
	

	/**
	 * Maini k�ynnist�� vehkeen
	 * @param args parametrit
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
