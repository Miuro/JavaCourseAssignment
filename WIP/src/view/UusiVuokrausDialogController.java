package view;

import fi.jyu.mit.fxgui.Dialogs;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class UusiVuokrausDialogController {

	@FXML
	private Button fxPeruutaButton;

	@FXML
	private Button fxVuokraaButton;

	private Stage dialogStage;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
	}

	/**
	 * Asettaa stageksi ("näyttämöksi") tämän ikkunan.
	 * 
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	/**
	 * Konstruktori taas.
	 */
	public UusiVuokrausDialogController() {
		// voipi olla tyhjää täynnä
	}

	/**
	 * Sulkee ikkunan.
	 */
	@FXML
	void handlePeruuta() {
		dialogStage.close();
	}

	/**
	 * 
	 */
	@FXML
	void handleVuokraaPyora() {
		Dialogs.showMessageDialog("Ei ole vielä lisätty");
	}

}
