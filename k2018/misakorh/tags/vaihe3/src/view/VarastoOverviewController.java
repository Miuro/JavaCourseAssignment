package view;

import pyoravarasto.MainApp;
import model.Pyora;
import fi.jyu.mit.fxgui.Dialogs;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;

public class VarastoOverviewController {

	@FXML
	private MenuItem fxMenuTallenna;

	@FXML
	private MenuItem fxMenuAvaa;

	@FXML
	private MenuItem fxMenuTulosta;

	@FXML
	private MenuItem fxMenuLopeta;

	@FXML
	private MenuItem fxMenuUusiPyora;

	@FXML
	private MenuItem fxMenuPoistaPyora;

	@FXML
	private MenuItem fxMenuApua;

	@FXML
	private MenuItem fxMenuTietoja;

	@FXML
	private Button fxUusiPyoraButton;

	@FXML
	private Button fxTallennaButton;

	@FXML
	private Button fxVuokraaButton;

	@FXML
	private Button fxPoistaButton;

	// Reference to the main application. En ole ihan varma mit‰ t‰m‰ tekee.
	private MainApp mainApp;

	/**
	 * Konstruktori
	 * T‰t‰ vissiin kutsutaan kun tapahtuu automaattinen initialize() metodi
	 * fxml-tiedoston ladattua. Dunno tbh
	 */
	public VarastoOverviewController() {

	}

	
	/**
	 * T‰t‰ kutsutaan MainApp:sta. M‰‰ritt‰‰, ett‰ t‰m‰ on perusn‰kym‰-
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	@FXML
    void handleAvaaApua() {
		Dialogs.showMessageDialog("Ei ole viel‰ lis‰tty");
    }

    @FXML
    void handleAvaaTietoja() {
    	Dialogs.showMessageDialog("Ei ole viel‰ lis‰tty");
    }

    @FXML
    void handleMenuAvaa() {
    	Dialogs.showMessageDialog("Ei ole viel‰ lis‰tty");
    }

    @FXML
    void handleMenuLopeta() {
    	Dialogs.showMessageDialog("Ei ole viel‰ lis‰tty");
    }

    @FXML
    void handleMenuTulosta() {
    	//Dialogs.showMessageDialog("Ei ole viel‰ lis‰tty");
		mainApp.showUusiTulostusDialog();
    }

	@FXML
	void handlePoistaPyora() {
		Dialogs.showMessageDialog("Ei ole viel‰ lis‰tty");
	}

	@FXML
	void handleTallenna() {
		Dialogs.showMessageDialog("Ei ole viel‰ lis‰tty");
	}

	@FXML
	void handleUusiPyora() {
		Dialogs.showMessageDialog("Ei ole viel‰ lis‰tty");
	}

	/**
	 * Avataan vuokrausikkuna.
	 */
	@FXML
	void handleUusiVuokraus() {
		mainApp.showUusiVuokrausDialog();
	}
	

}
