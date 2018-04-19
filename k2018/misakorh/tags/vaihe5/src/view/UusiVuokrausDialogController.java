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
	 * Asettaa stageksi ("n�ytt�m�ksi") t�m�n ikkunan.
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
		// voipi olla tyhj�� t�ynn�
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
		Dialogs.showMessageDialog("Ei ole viel� lis�tty");
	}

}