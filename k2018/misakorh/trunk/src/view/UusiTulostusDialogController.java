package view;

import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebEngine;


/**
 * Luonti uudelle tulostusdialogille.
 * @author Miro Korhonen
 * @version 1.0, 15.5.2018
 */
public class UusiTulostusDialogController implements ModalControllerInterface<String> {
	
	@FXML
	TextArea tulostusAlue;

	/**
	 * Sulkee ikkunan.
	 */
	@FXML
	void handleOk() {
		ModalController.closeStage(tulostusAlue);
	}
	
	
	@FXML
	void handleTulosta() {
		PrinterJob job = PrinterJob.createPrinterJob();
		if (job != null && job.showPrintDialog(null)) {
			WebEngine webEngine = new WebEngine();
			webEngine.loadContent("<pre>" + tulostusAlue.getText() + "</pre>");
			webEngine.print(job);
			job.endJob();
		}
	}

	
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