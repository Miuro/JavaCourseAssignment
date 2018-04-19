package view;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;

public class UusiTulostusDialogController implements ModalControllerInterface<String> {
	
	@FXML
	TextArea tulostusAlue;
	//@FXML
	//private Button fxTulostaButton;
	//@FXML
	//private Button fxOkButton;

	//private Stage dialogStage;
	
	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	//@FXML
	//private void Initialize(){
	//}
	
	
	/**
	 * Sulkee ikkunan.
	 */
	@FXML
	void handleOk() {
		ModalController.closeStage(tulostusAlue);
	}
	
	
	@FXML
	void handleTulosta() {
		//Dialogs.showMessageDialog("Ei ole viel‰ lis‰tty");
		PrinterJob job = PrinterJob.createPrinterJob();
		if (job != null && job.showPrintDialog(null)) {
			WebEngine webEngine = new WebEngine();
			webEngine.loadContent("<pre>" + tulostusAlue.getText() + "</pre>");
			webEngine.print(job);
			job.endJob();
		}
	}
	
	
	/**
	 * Konstruktori.
	 * */
//	public UusiTulostusDialogController(){
		
	//}
	
    /**
     * @return alue johon tulostetaan
     */
    public TextArea getTextArea() {
        return tulostusAlue;
    }
    
    /**
     * N‰ytt‰‰ tulostusalueeseen tekstin
     * @param tulostus n‰ytett‰v‰ teksti
     * @return control kontrolleri jolta voi hjalutessa pyyt‰‰ lis‰‰ tietoja
     */
    public static UusiTulostusDialogController tulosta(String tulostus) {
    	UusiTulostusDialogController control = 
          (UusiTulostusDialogController) ModalController.showModeless(UusiTulostusDialogController.class.getResource("UusiTulostusDialog.fxml"),
                  "Tulostus", tulostus);
    	return control;
    }

	@Override
	public String getResult() {
		return null;
	}

	/**
	 * Mit‰ tehd‰‰n kun dialogi on n‰ytetty
	 */
	@Override
	public void handleShown() {
		//
		
	}

	@Override
	public void setDefault(String oletus) {
		tulostusAlue.setText(oletus);
	}
	
}