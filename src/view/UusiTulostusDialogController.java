package view;

import fi.jyu.mit.fxgui.Dialogs;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class UusiTulostusDialogController {
	
	@FXML
	TextArea tulostusAlue;
	
	@FXML
	private Button fxTulostaButton;

	@FXML
	private Button fxOkButton;

	private Stage dialogStage;
	
	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void Initialize(){
	}
	
	/**
	 * Asettaa stageksi ("näyttämöksi") tämän ikkunan.
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	
	
	/**
	 * Sulkee ikkunan.
	 */
	@FXML
	void handleOk() {
		dialogStage.close();
	}
	
	
	@FXML
	void handleTulosta() {
		Dialogs.showMessageDialog("Ei ole vielä lisätty");
	}
	
	
	/**
	 * Konstruktori.
	 * */
	public UusiTulostusDialogController(){
		
	}
	
    /**
     * @return alue johon tulostetaan
     */
    public TextArea getTextArea() {
        return tulostusAlue;
    }
    
    /**
     * Antaa UusiTulostusDialogControllerin
     * @param tulostus
     * @return
     */
    public static UusiTulostusDialogController tulosta (String tulostus) {
    	UusiTulostusDialogController control = new UusiTulostusDialogController();
    	
    	return control;
    }
	
	
}
