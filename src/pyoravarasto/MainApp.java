package pyoravarasto;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import view.UusiTulostusDialogController;
import view.UusiVuokrausDialogController;
import view.VarastoOverviewController;

public class MainApp extends Application {
	
	private Stage primaryStage;
	private BorderPane overview;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
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
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Luonti vuokrausikkunalle.
	 */
	public void showUusiVuokrausDialog() {
		try {
			// Ladataan fxml-tiedosto ja luodaan uusi stage sille
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainApp.class.getResource("/view/UusiVuokrausDialog.fxml"));
	        BorderPane page = (BorderPane) loader.load();
	        
	        // Luodaan dialog, eli ikkuna, stage, mutta niin, ett� p��ikkuna n�kyy edelleen (modality juttui)
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Vuokraus");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(primaryStage);
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);
	        
	        // Haetaan controller ja yhdistet��n se stagen kanssa.
	        UusiVuokrausDialogController controller = loader.getController();
	        controller.setDialogStage(dialogStage);
	        
	        dialogStage.showAndWait();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Luonti tulostusdialogille
	 * */
	public void showUusiTulostusDialog() {
		try {
			// Ladataan fxml-tiedosto ja luodaan uusi stage sille
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainApp.class.getResource("/view/UusiTulostusDialog.fxml"));
	        BorderPane page = (BorderPane) loader.load();
	        
	        // Luodaan dialog, eli ikkuna, stage, mutta niin, ett� p��ikkuna n�kyy edelleen (modality juttui)
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Tulostus");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(primaryStage);
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);
	        
	        // Haetaan controller ja yhdistet��n se stagen kanssa.
	        UusiTulostusDialogController controller = loader.getController();
	        controller.setDialogStage(dialogStage);
	        
	        dialogStage.showAndWait();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}



	public static void main(String[] args) {
		launch(args);
	}
}
